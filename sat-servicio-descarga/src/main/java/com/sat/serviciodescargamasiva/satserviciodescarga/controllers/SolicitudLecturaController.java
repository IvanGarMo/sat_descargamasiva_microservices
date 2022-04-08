/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.controllers;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.Cliente;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.FiltroBusqueda;
import com.sat.serviciodescargamasiva.satserviciodescarga.jdbc.OperacionesAutorizacion;
import com.sat.serviciodescargamasiva.satserviciodescarga.jdbc.OperacionesCliente;
import com.sat.serviciodescargamasiva.satserviciodescarga.jdbc.OperacionesSolicitud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author IvanGarMo
 */
@RestController
@RequestMapping(path="/solicitudes")
public class SolicitudLecturaController {
    @Autowired
    private OperacionesAutorizacion authService;
    @Autowired
    private OperacionesSolicitud solicitudRepo;
    @Autowired
    private OperacionesCliente clienteRepo;
    
    @PostMapping(path="/lista")
    public ResponseEntity<Object> getLista(@RequestHeader("uuid") String uuid, 
            FiltroBusqueda filtroBusqueda) {
        try {
            Object object = solicitudRepo.listaSolicitudes(uuid, filtroBusqueda);
            return new ResponseEntity<>(object, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(path="/estados")
    public ResponseEntity<Object> getEstado() {
        try {
            Object object = solicitudRepo.listaEstados();
            return new ResponseEntity<>(object, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(path="/clientes/{id}")
    public ResponseEntity<Cliente> getCliente(@RequestHeader("uuid") String uuid, @PathVariable("id") long idCliente) {
        try {
            Cliente cliente = clienteRepo.getCliente(idCliente);
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
