/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.controller;

import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.data.ClienteVista;
import com.sat.serviciodescargamasiva.satclientes.data.FiltroCliente;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesCliente;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sat.serviciodescargamasiva.satclientes.jdbc.AutorizacionOperacion;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author IvanGarMo
 */
@RestController
@RequestMapping(path="/clientes", produces="application/json")
@CrossOrigin(origins = "*")
@Slf4j
public class ClienteController {
    @Autowired
    private OperacionesCliente operacionesCliente;
    private AutorizacionOperacion autorizaOperacion;
    private boolean suboCertificadoNube = false;
    
    private ObjetoValidacion objValidacion;
    
    public ClienteController(OperacionesCliente operacionesCliente) {
        this.operacionesCliente = operacionesCliente;
    }
    
    @PostMapping(consumes="application/json")
    public ResponseEntity<ResponseData> guardaCliente(@RequestHeader("uuid") String uuid, 
            @Valid @RequestBody Cliente cliente) {
        objValidacion = validaAcceso(uuid, cliente.getIdCliente());
        if(!objValidacion.isObjetoValido()) {
            return objValidacion.getResponseEntity();
        }
        
        ResponseData rd = operacionesCliente.guardaCliente(uuid, cliente);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @PatchMapping
    public ResponseEntity<ResponseData> actualizaCliente(@RequestHeader("uuid") String uuid, 
            @RequestBody Cliente cliente) {
        objValidacion = validaAcceso(uuid, cliente.getIdCliente());
        if(!objValidacion.isObjetoValido()) {
            return objValidacion.getResponseEntity();
        }
        
        ResponseData rd = operacionesCliente.actualizaCliente(uuid, cliente);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @DeleteMapping(path="/{idCliente}")
    public ResponseEntity<ResponseData> eliminaCliente(@RequestHeader("uuid") String uuid, 
            @PathVariable("idCliente") int idCliente) {
        objValidacion = validaAcceso(uuid, idCliente);
        if(!objValidacion.isObjetoValido()) {
            return objValidacion.getResponseEntity();
        }
        
        ResponseData rd = operacionesCliente.eliminaCliente(uuid, idCliente);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @PostMapping(path="/contrasena")
    public ResponseEntity<ResponseData> actualizaContrasena(@RequestHeader("uuid") String uuid, 
            @RequestBody Cliente cliente) {
        objValidacion = validaAcceso(uuid, cliente.getIdCliente());
        if(!objValidacion.isObjetoValido()) {
            return objValidacion.getResponseEntity();
        }
        
        ResponseData rd = operacionesCliente.guardaContrasenaCliente(uuid, cliente.getIdCliente(), 
                cliente.getContrasena());
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @PostMapping(path="/certificado")
    public ResponseEntity<ResponseData> actualizaCertificado(@RequestHeader("uuid") String uuid, 
            @RequestBody Cliente cliente) {
        objValidacion = validaAcceso(uuid, cliente.getIdCliente());
        if(!objValidacion.isObjetoValido()) {
            return objValidacion.getResponseEntity();
        }

        ResponseData rd = new ResponseData();
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @GetMapping(path="/{idCliente}")
    public ResponseEntity<Cliente> cargaCliente(@RequestHeader("uuid") String uuid, @PathVariable long idCliente) {
                objValidacion = validaAcceso(uuid, idCliente);
        if(!objValidacion.isObjetoValido()) {
            return new ResponseEntity<>(new Cliente(), HttpStatus.FORBIDDEN);
        }
        
        try {
            Cliente c = operacionesCliente.getCliente(idCliente);
            return new ResponseEntity<>(c, HttpStatus.OK);    
        } catch(NullPointerException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping(path="/lista")
    public ResponseEntity<Object> cargaListaClientes(@RequestHeader("uuid") String uuid, 
            @RequestBody FiltroCliente filtroCliente) {
        Object clientes = operacionesCliente.getClientes(uuid, 
                filtroCliente.getRfc(), filtroCliente.getNombre(), filtroCliente.getApPaterno(), 
                filtroCliente.getApMaterno());
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    private ObjetoValidacion validaAcceso(String uuid, long idCliente) {
        ResponseData rd = autorizaOperacion.puedeAcceder(uuid, idCliente);
        ObjetoValidacion objValidacion = new ObjetoValidacion();
        objValidacion.setObjetoValido(rd.isOpValida());
        if(!rd.isOpValida()) {
            ResponseEntity respuestaNegativa = new ResponseEntity<>(rd, HttpStatus.FORBIDDEN);
            objValidacion.setResponseEntity(respuestaNegativa);
        }
        return objValidacion;
    }
 }

 //Solamente para pasar información más rápido entre los métodos
 
    @Data
    @NoArgsConstructor
    class ObjetoValidacion { 
        private boolean objetoValido;
        private ResponseEntity<ResponseData> responseEntity;
    }


