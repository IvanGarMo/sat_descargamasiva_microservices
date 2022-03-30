/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.controllers;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
import com.sat.serviciodescargamasiva.satusuarios.data.Usuario;
import com.sat.serviciodescargamasiva.satusuarios.jdbc.OperacionesUsuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author IvanGarMo
 */
@RestController
@RequestMapping(path="/registro", produces="application/json")
@Slf4j
public class RegistroController {
    @Autowired
    private OperacionesUsuario opUsuario;
    
    @PostMapping
    public ResponseEntity<ResponseData> registrarUsuario(@RequestBody Usuario cuerpoUsuario) {
        ResponseData rd = opUsuario.registraUsuario(cuerpoUsuario);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
}
