/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.controllers;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
import com.sat.serviciodescargamasiva.satusuarios.data.Usuario;
import com.sat.serviciodescargamasiva.satusuarios.jdbc.OperacionesUsuario;
import com.sat.serviciodescargamasiva.satusuarios.jpa.JpaUsuario;
import java.util.Optional;
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
@RequestMapping(path="/usuarios", produces="application/json")
public class UsuarioController {
    @Autowired
    private JpaUsuario repo;
    @Autowired
    private OperacionesUsuario opUsuario;
    
    @GetMapping
    public ResponseEntity<Usuario> cargaUsuario(@RequestHeader("uuid") String uuid) {
        Optional<Usuario> usuario = repo.findByUid(uuid);
        if(usuario.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<ResponseData> actualizaUsuario(@RequestHeader("uuid") String uuid, @RequestBody Usuario u) {
        u.setUid(uuid);
        ResponseData rd = opUsuario.actualizaUsuario(u);
        return new ResponseEntity<>(rd, HttpStatus.OK);
    }
    
}
