/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satsuscripciones.controllers;

import com.sat.serviciodescargamasiva.satsuscripciones.data.ResponseData;
import com.sat.serviciodescargamasiva.satsuscripciones.data.Suscripcion;
import com.sat.serviciodescargamasiva.satsuscripciones.jdbc.OperacionesSuscripciones;
import com.sat.serviciodescargamasiva.satsuscripciones.jpa.SuscripcionJpa;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping(path="/suscripciones", produces="application/json")
@CrossOrigin(origins="*")
public class SuscripcionController {
    @Autowired
    private SuscripcionJpa jpa;
    @Autowired
    private OperacionesSuscripciones opSus;
    
    @GetMapping
    public ResponseEntity<Iterable<Suscripcion>> getSuscripciones() {
        Iterable<Suscripcion> sus = jpa.findAll();
        return new ResponseEntity<>(sus, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<ResponseData> cambiaSuscripcion(@RequestHeader("uuid") String uuid, 
            @RequestBody Suscripcion suscripcion) {
        ResponseData rd = opSus.cambiaSuscripcionUsuario(uuid, suscripcion.getIdSuscripcion());
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @GetMapping("/cliente")
    public ResponseEntity<Integer> suscripcionUsuario(@RequestHeader("uuid") String uuid) {
        Integer sus = opSus.getSuscripcionPorUsuario(uuid);
        return new ResponseEntity<>(sus, HttpStatus.OK);
    }
}
