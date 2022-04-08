/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.controller;

import com.sat.serviciodescargamasiva.satclientes.data.MedioContacto;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import com.sat.serviciodescargamasiva.satclientes.jdbc.AutorizacionOperacion;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesCliente;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesMedioContacto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping(path="/clientes/contacto", produces="application/json")
@CrossOrigin(origins="*")
@Slf4j
public class MedioContactoController {
    @Autowired
    private AutorizacionOperacion autorizaService;
    @Autowired
    private OperacionesMedioContacto medioRepo;
    
    @GetMapping("/lista")
    public ResponseEntity<Object> getLista() {
        Object obj = medioRepo.cargaListaMedioContacto();
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    
    @PostMapping(path="/registrados")
    public ResponseEntity<Object> cargaListaCliente(@RequestHeader("uuid") String uuid, 
                @RequestBody MedioContacto medio) {
        ResponseData rd = autorizaService.puedeAcceder(uuid, medio.getIdCliente());
        if(!rd.isOpValida()) {
            return new ResponseEntity<>(rd, HttpStatus.FORBIDDEN);
        }
        if(!autorizaService.puedeRealizarOperacionMedioContacto(medio)) {
            return new ResponseEntity<>(rd, HttpStatus.FORBIDDEN);
        }
        
        Object obj = medioRepo.cargaListaMedioContactoCliente(medio.getIdCliente());
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<ResponseData> salvaMedioContactoCliente(@RequestHeader("uuid") String uuid, 
            @RequestBody MedioContacto medio) {
        ResponseData rd = autorizaService.puedeAcceder(uuid, medio.getIdCliente());
        if(!rd.isOpValida()) {
            return new ResponseEntity<>(rd, HttpStatus.FORBIDDEN);
        }
        if(!autorizaService.puedeRealizarOperacionMedioContacto(medio)) {
            return new ResponseEntity<>(rd, HttpStatus.FORBIDDEN);
        }
        
        rd = medioRepo.insertaMedioContactoCliente(medio.getIdCliente(), medio);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @PatchMapping
    public ResponseEntity<ResponseData> actualizaMedioContactoCliente(@RequestHeader("uuid") String uuid, 
            @RequestBody MedioContacto medio) {
        ResponseData rd = autorizaService.puedeAcceder(uuid, medio.getIdCliente());
        if(!rd.isOpValida()) {
            return new ResponseEntity<>(rd, HttpStatus.FORBIDDEN);
        }
        if(!autorizaService.puedeRealizarOperacionMedioContacto(medio)) {
            return new ResponseEntity<>(rd, HttpStatus.FORBIDDEN);
        }
        rd = medioRepo.actualizaMedioContactoCliente(medio.getIdCliente(), medio);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @DeleteMapping
    public ResponseEntity<ResponseData> eliminaMedioContactoCliente(@RequestHeader("uuid") String uuid, 
            @RequestBody MedioContacto medio) {
        ResponseData rd = autorizaService.puedeAcceder(uuid, medio.getIdCliente());
        if(!rd.isOpValida()) {
            return new ResponseEntity<>(rd, HttpStatus.FORBIDDEN);
        }
        if(!autorizaService.puedeRealizarOperacionMedioContacto(medio)) {
            return new ResponseEntity<>(rd, HttpStatus.FORBIDDEN);
        }
        
        rd = medioRepo.eliminaMedioContactoCliente(medio.getIdCliente(), medio);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
}
