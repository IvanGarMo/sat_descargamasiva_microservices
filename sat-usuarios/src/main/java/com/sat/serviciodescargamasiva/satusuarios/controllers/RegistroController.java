/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.controllers;

import com.sat.serviciodescargamasiva.satusuarios.data.Usuario;
import com.sat.serviciodescargamasiva.satusuarios.jdbc.OperacionesUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author elda_
 */
@RestController
@RequestMapping(path="/registro", produces="application/json")
public class RegistroController {
    @Autowired
    private OperacionesUsuario opUsuario;
    
    @PostMapping
    public void registrarUsuario(@RequestBody Usuario cuerpoUsuario) {
        opUsuario.registraUsuario(cuerpoUsuario);
    }
}
