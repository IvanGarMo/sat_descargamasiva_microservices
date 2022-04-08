/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.controller;

import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesContrasenaSat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping(path="/contrasenas", produces="application/json")
@CrossOrigin(origins = "*")
@Slf4j
public class ContrasenaController {
    @Autowired
    private OperacionesContrasenaSat operacionesContrasena;
    
    @PostMapping
    public ResponseEntity<ResponseData> actualizaContrasena(@RequestHeader("uuid") String uuid, 
            @RequestBody Cliente cliente) {
        ResponseData rd = operacionesContrasena.guardaContrasenaCliente(uuid, cliente.getIdCliente(), 
                cliente.getContrasena());
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
}
