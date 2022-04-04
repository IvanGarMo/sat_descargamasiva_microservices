/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.controllers;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
import com.sat.serviciodescargamasiva.satusuarios.data.Telefono;
import com.sat.serviciodescargamasiva.satusuarios.jdbc.OperacionesTelefono;
import com.sat.serviciodescargamasiva.satusuarios.jpa.JpaTelefono;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(path="/telefonos", produces="application/json")
@CrossOrigin(origins="*")
@Slf4j
public class TelefonoController {
    @Autowired
    private OperacionesTelefono telefonoRepo;
    
    @GetMapping
    public ResponseEntity<Object> getTelefonos(@RequestHeader("uuid") String uuid) {
        try {
            Object object = telefonoRepo.listaTelefonosUsuario(uuid);
            return new ResponseEntity(object, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping
    public ResponseEntity<ResponseData> capturaTelefono(@RequestHeader("uuid") String uuid, 
            @RequestBody Telefono telefono) {
        try {
            ResponseData rd = telefonoRepo.capturaTelefonoUsuario(uuid, telefono.getTelefono());
            return new ResponseEntity(rd, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping
    public ResponseEntity<ResponseData> eliminaTelefono(@RequestHeader("uuid") String uuid, 
            @RequestBody Telefono telefono) {        
        try {
            ResponseData rd = telefonoRepo.eliminaTelefonoUsuario(uuid, telefono.getIdTelefono());
            return new ResponseEntity(rd, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
}
