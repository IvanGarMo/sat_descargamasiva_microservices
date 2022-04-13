/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.controllers;

import com.sat.serviciodescargamasiva.satserviciodescarga.jdbc.OperacionesSolicitud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author IvanGarMo
 */
@RestController
@RequestMapping(path="/opciones")
@CrossOrigin(origins = "*")
public class OpcionesController {
    @Autowired
    private OperacionesSolicitud solicitudRepo;
    
    @GetMapping(path="/complemento")
    public ResponseEntity<Object> getComplemento() {
        Object obj = solicitudRepo.getComplementos();
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    
    @GetMapping(path="/estadoComprobante")
    public ResponseEntity<Object> getEstadoComprobante() {
        Object obj = solicitudRepo.getSolicitudDescargaEstado();
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    
    @GetMapping(path="/tipo")
    public ResponseEntity<Object> getTipoSolicitud() {
        Object obj = solicitudRepo.getSolicitudDescargaTipoSolicitud();
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    
    @GetMapping(path="/estadoSolicitud") 
    public ResponseEntity<Object> getListaEstadoSolicitud() {
        Object obj = solicitudRepo.listaEstados();
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
}
