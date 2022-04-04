/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.controllers;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.ContrasenaCertificado;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.Solicitud;
import com.sat.serviciodescargamasiva.satserviciodescarga.jdbc.OperacionesAutorizacion;
import com.sat.serviciodescargamasiva.satserviciodescarga.jdbc.OperacionesSolicitud;
import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.AuthenticationProvider;
import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.RequestProvider;
import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.TipoSolicitud;
import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.Resultado;
import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.ResultadoSolicitudDescarga;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author IvanGarMo
 */
@RestController
@RequestMapping(path="/solicitudes", produces="application/json")
public class SolicitudController {
    @Autowired
    private RequestProvider solicitudConnector;
    @Autowired
    private AuthenticationProvider authProvider;
    @Autowired
    private OperacionesSolicitud solicitudRepo;
    @Autowired
    private OperacionesAutorizacion authRepo;
    
    @GetMapping(path="/{idSolicitud}")
    public ResponseEntity<Solicitud> getSolicitud(@PathVariable("idSolicitud") long idSolicitud, 
            @RequestHeader("uuid") String uuid) {
        if(authRepo.tieneAccesoSolicitud(idSolicitud, uuid)) {
            Solicitud solicitud = solicitudRepo.cargaDetalleSolicitud(idSolicitud);
            return new ResponseEntity<>(solicitud, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
    
    
    @PostMapping
    public ResponseEntity<Resultado> realizaSolicitud(@RequestHeader("uuid") String uuid, 
            @RequestBody Solicitud solicitud, 
            @CookieValue(value = "token_sat", defaultValue = "") String tokenSat) {
        Resultado resultadoOperacion;
        
        //Primero, es necesario validar que el usuario tenga acceso al cliente
        if(authRepo.tieneAccesoCliente(solicitud.getIdCliente(), uuid)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        
        //Luego, es necesario validar que no haya ninguna otra solicitud con los mismos parámetros
        resultadoOperacion = existeSolicitud(solicitud);
        if(resultadoOperacion.isOperacionCorrecta()) { //Notese que aquí es solamente SÍ existe, y el método regresara true si es que existe
            return new ResponseEntity<>(resultadoOperacion, HttpStatus.BAD_REQUEST);
        }
        
        //Es necesario validar que todos los campos de la solicitud estén en orden
        //Si no, es enviado de regreso
        resultadoOperacion = this.validar(solicitud);
        if(!resultadoOperacion.isOperacionCorrecta()) 
            return new ResponseEntity<>(resultadoOperacion, HttpStatus.BAD_REQUEST);
        
        //Luego, es necesario validar que el cliente tenga tanto contraseña como certificado
        resultadoOperacion = this.tieneCertificado(solicitud);
        if(!resultadoOperacion.isOperacionCorrecta()) {
            return new ResponseEntity<>(resultadoOperacion, HttpStatus.BAD_REQUEST);
        }
        
        resultadoOperacion = this.tieneContrasena(solicitud);
        if(!resultadoOperacion.isOperacionCorrecta()) {
            return new ResponseEntity<>(resultadoOperacion, HttpStatus.BAD_REQUEST);
        }
        
        //Obtengo el certificado y la llave del cliente
        ContrasenaCertificado contCert = authRepo.cargaDatosAutenticacion(solicitud.getIdCliente());
        InputStream inputStream = new ByteArrayInputStream(contCert.getCertificado());
        char[] passwordPFX = contCert.getContrasena().toCharArray();
        
        X509Certificate certificate;
        PrivateKey privateKey;
        try {
            certificate =  authProvider.getCertificate(inputStream, passwordPFX);
            privateKey = authProvider.getPrivateKey(inputStream, passwordPFX);
        } catch(KeyStoreException ex) {
            String mensaje = "Ha habido un fallo. Por favor, revise que esté usando el certificado y contraseñas correctas";
            resultadoOperacion = new Resultado();
            resultadoOperacion.setOperacionCorrecta(false);
            resultadoOperacion.setMensaje(mensaje);
            return new ResponseEntity<>(resultadoOperacion, HttpStatus.BAD_REQUEST);
        } catch(IOException ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch(CertificateException ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch(NoSuchAlgorithmException ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch(UnrecoverableKeyException ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch(Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        
        //Realizo la comunicación con el SAT
        try {
            resultadoOperacion = solicitudConnector.doRequest(certificate, 
                        privateKey, 
                        solicitud.getRfcSolicitante(),
                        solicitud.getRfcEmisor(),
                        solicitud.getRfcReceptor(),
                        solicitud.getFechaInicioPeriodo(),
                        solicitud.getFechaFinPeriodo(),
                        TipoSolicitud.CFDI
                        );
        } catch(Exception ex) {
            
        }
        
        //Guardo resultado en la base de datos
        ResponseData rd = solicitudRepo.guardaSolicitud(solicitud);
        resultadoOperacion.setMensaje(rd.getMensaje());
        resultadoOperacion.setOperacionCorrecta(rd.isActivo());
        return new ResponseEntity<>(resultadoOperacion, HttpStatus.OK);
    }
    
    
    private Resultado validar(Solicitud solicitud) {
        return solicitudConnector.validate(
                solicitud.getRfcSolicitante(),
                solicitud.getRfcEmisor(),
                solicitud.getRfcReceptor(),
                solicitud.getfechaInicioPeriodo(),
                solicitud.getFechaFinPeriodo(),
                TipoSolicitud.CFDI
        );
    }
    
    private Resultado tieneCertificado(Solicitud solicitud) {
        ResponseData rd = authRepo.tieneCertificado(solicitud.getIdCliente());
        Resultado res = new Resultado();
        res.setOperacionCorrecta(rd.isActivo());
        res.setMensaje(rd.getMensaje());
        return res;
    }
    
    private Resultado tieneContrasena(Solicitud solicitud) {
        ResponseData rd = authRepo.tieneContrasena(solicitud.getIdCliente());
        Resultado res = new Resultado();
        res.setOperacionCorrecta(rd.isActivo());
        res.setMensaje(rd.getMensaje());
        return res;
    }
    
    private Resultado existeSolicitud(Solicitud solicitud) {
        ResponseData rd = authRepo.existeSolicitud(solicitud);
        Resultado res = new Resultado();
        res.setOperacionCorrecta(rd.isActivo());
        res.setMensaje(rd.getMensaje());
        return res;
    }
}