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
@RequestMapping("/suscripciones")
public class SuscripcionController {
    @Autowired
    private SuscripcionJpa suscripcionRepo;
    @Autowired
    private OperacionesSuscripciones operacionesSus;
    
    @GetMapping
    public ResponseEntity<Iterable<Suscripcion>> getSuscripciones() {
        Iterable<Suscripcion> suscripciones = suscripcionRepo.findAll();
        return new ResponseEntity<>(suscripciones, HttpStatus.OK);
    }
    
    @GetMapping("/usuario")
    public ResponseEntity<Suscripcion> getSuscripcionUsuario(@RequestHeader("uuid") String uuid) {
        Suscripcion sus;
        try {
            sus = operacionesSus.getSuscripcionPorUsuario(uuid);
        } catch(Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sus, HttpStatus.OK);
    }
    
    @PostMapping("/usuario")
    public ResponseEntity<ResponseData> cambiaSuscripcionUsuario(@RequestHeader("uuid") String uuid, 
            @RequestBody Suscripcion suscripcion) {
        ResponseData rd = operacionesSus.cambiaSuscripcionUsuario(uuid, suscripcion.getIdSuscripcion());
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
}
