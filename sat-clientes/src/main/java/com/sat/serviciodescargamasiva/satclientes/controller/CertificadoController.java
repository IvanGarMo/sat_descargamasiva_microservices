/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.controller;

import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesCertificado;
import java.io.IOException;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
/**
 *
 * @author IvanGarMo
 */
@RestController
@RequestMapping(path="/certificados", produces="application/json")
@CrossOrigin(origins = "*")
@Slf4j
public class CertificadoController {
    @Autowired
    private OperacionesCertificado opCertificado;
    @Autowired
    private Environment environment;
    
    @PostMapping
    public ResponseEntity<ResponseData> actualizaCertificado(@RequestHeader("uuid") String uuid,
            @ModelAttribute Cliente cliente) throws IOException {
        //boolean CERTIFICADO_ALMACENAMIENTO_NUBE = Boolean.parseBoolean(environment.getProperty("certificado.almacenamiento.nube"));
        boolean CERTIFICADO_ALMACENAMIENTO_NUBE = true;
        ResponseData rd;
        
//        try {
            if (CERTIFICADO_ALMACENAMIENTO_NUBE) {
                rd = opCertificado.guardaCertificadoBlob(uuid, cliente);
            } else {
                rd = opCertificado.guardaCertificadoBaseDatos(uuid, cliente);
            }
            return new ResponseEntity<>(rd, HttpStatus.OK);
//        } catch (IOException ex) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }
}
