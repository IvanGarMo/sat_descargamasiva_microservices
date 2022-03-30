/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.controllers;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
import com.sat.serviciodescargamasiva.satusuarios.data.Telefono;
import com.sat.serviciodescargamasiva.satusuarios.jdbc.OperacionesTelefono;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class TelefonoController {

    @Autowired
    private OperacionesTelefono telefonoRepo;

    @GetMapping
    public ResponseEntity<List<Telefono>> getTelefonos(@RequestHeader("uuid") String uuid) {
        List<Telefono> telefonos = telefonoRepo.listaTelefonosUsuario(uuid);
        if(telefonos != null && telefonos.size() > 0) {
            return new ResponseEntity<>(telefonos, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    
    @PostMapping
    public ResponseEntity<ResponseData> capturaTelefono(@RequestHeader("uuid") String uuid, String telefono) {
        ResponseData rd = telefonoRepo.capturaTelefonoUsuario(uuid, telefono);
        return new ResponseEntity(rd, HttpStatus.OK);
    }
    
    @DeleteMapping
    public ResponseEntity<ResponseData> eliminaTelefono(@RequestHeader("uuid") String uuid, @RequestBody Telefono telefono) {
        ResponseData rd = telefonoRepo.eliminaTelefonoUsuario(uuid, telefono.getIdTelefono());
        return new ResponseEntity(rd, HttpStatus.OK);
    }
}
