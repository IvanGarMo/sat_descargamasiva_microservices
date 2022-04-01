/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.controller;

import com.sat.serviciodescargamasiva.satclientes.data.Cliente;
import com.sat.serviciodescargamasiva.satclientes.data.ClienteVista;
import com.sat.serviciodescargamasiva.satclientes.data.FiltroCliente;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesCliente;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(path="/clientes", produces="application/json")
@CrossOrigin(origins = "*")
@Slf4j
public class ClienteController {
    @Autowired
    private OperacionesCliente operacionesCliente;
    
    @PostMapping(consumes="application/json")
    public ResponseEntity<ResponseData> guardaCliente(@RequestHeader("uuid") String uuid, 
            @Valid @RequestBody Cliente cliente) {
        ResponseData rd = operacionesCliente.guardaCliente(uuid, cliente);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @PatchMapping
    public ResponseEntity<ResponseData> actualizaCliente(@RequestHeader("uuid") String uuid, 
            @RequestBody Cliente cliente) {
        ResponseData rd = operacionesCliente.actualizaCliente(uuid, cliente);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @DeleteMapping(path="/{idCliente}")
    public ResponseEntity<ResponseData> eliminaCliente(@RequestHeader("uuid") String uuid, 
            @PathVariable("idCliente") int idCliente) {
        ResponseData rd = operacionesCliente.eliminaCliente(uuid, idCliente);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @PostMapping(path="/contrasena")
    public ResponseEntity<ResponseData> actualizaContrasena(@RequestHeader("uuid") String uuid, 
            @RequestBody Cliente cliente) {
        ResponseData rd = operacionesCliente.guardaContrasenaCliente(uuid, cliente.getIdCliente(), 
                cliente.getContrasena());
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @PostMapping(path="/certificado")
    public ResponseEntity<ResponseData> actualizaCertificado(@RequestHeader("uuid") String uuid, 
            @RequestBody Cliente cliente) {
        ResponseData rd = operacionesCliente.guardaCertificadoCliente(uuid, cliente.getIdCliente(), 
                cliente.getCertificado());
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
    @GetMapping(path="/{idCliente}")
    public ResponseEntity<Cliente> cargaCliente(@RequestHeader("uuid") String uuid, @PathVariable long idCliente) {
        try {
            Cliente c = operacionesCliente.getCliente(idCliente);
            return new ResponseEntity<>(c, HttpStatus.OK);    
        } catch(NullPointerException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping(path="/lista")
    public ResponseEntity<Object> cargaListaClientes(@RequestHeader("uuid") String uuid, 
            @RequestBody FiltroCliente filtroCliente) {
        Object clientes = operacionesCliente.getClientes(uuid, 
                filtroCliente.getRfc(), filtroCliente.getNombre(), filtroCliente.getApPaterno(), 
                filtroCliente.getApMaterno());
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }
 }
