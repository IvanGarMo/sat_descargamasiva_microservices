/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.controller;

import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesKey;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
/**
 *
 * @author IvanGarMo
 */
@RestController
@RequestMapping(path="/keys", produces="application/json")
@CrossOrigin(origins = "*")
@Slf4j
public class KeyController {
    @Autowired
    private OperacionesKey keyRepo;
    
    @PostMapping
    public ResponseEntity<ResponseData> actualizaCertificado(@RequestHeader("uuid") String uuid,
            @ModelAttribute Cliente cliente) throws IOException {
        //boolean CERTIFICADO_ALMACENAMIENTO_NUBE = Boolean.parseBoolean(environment.getProperty("certificado.almacenamiento.nube"));
        boolean CERTIFICADO_ALMACENAMIENTO_NUBE = true;
        ResponseData rd;
        
//        try {
            if (CERTIFICADO_ALMACENAMIENTO_NUBE) {
                rd = keyRepo.guardaKeyNube(cliente);;
            } else {
                rd = keyRepo.guardaKeyLocal(cliente.getIdCliente(), cliente.getKey().getBytes());
            }
            return new ResponseEntity<>(rd, HttpStatus.OK);
//        } catch (IOException ex) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }
}
