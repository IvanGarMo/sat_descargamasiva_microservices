/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.controllers;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
        import com.sat.serviciodescargamasiva.satusuarios.data.Usuario;
import com.sat.serviciodescargamasiva.satusuarios.jdbc.OperacionesRegistro;
import com.sat.serviciodescargamasiva.satusuarios.jdbc.OperacionesUsuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping(path="/registro", produces="application/json")
@Slf4j
public class RegistroController {
    @Autowired
    private OperacionesRegistro registroRepo;
    
    //@CrossOrigin
    @PostMapping(path="/nuevo", consumes="application/json")
    public ResponseEntity<Object> creaNuevoUsuario(@RequestBody Usuario usuario) {
        try {
            registroRepo.salvaNuevoUsuario(usuario);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //@CrossOrigin
    @PostMapping("/activaCorreo")
    public ResponseEntity<Object> activaCorreo(@RequestBody Usuario usuario) {
        try {
            log.info("ActivaCorreo - Uid "+usuario.getUid()+" CorreoConfirmado "+usuario.getCorreoConfirmado());
            registroRepo.activaCorreo(usuario);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/activaUsuario") 
    public ResponseEntity<Usuario> estaUsuarioActivo(@RequestHeader("uuid") String uidFirebase) {
        log.info("ActivaUsuario: "+uidFirebase);
        try {
            Usuario u = registroRepo.estaUsuarioActivo(uidFirebase);
            return new ResponseEntity<>(u, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //@CrossOrigin
    @PostMapping(path="/terminaRegistro")
    public ResponseEntity<ResponseData> terminaRegistro(@RequestHeader("uuid") String uidFirebase, @RequestBody Usuario cuerpoUsuario) {
        cuerpoUsuario.setUid(uidFirebase);
        try {
            ResponseData rd = registroRepo.terminaRegistro(cuerpoUsuario);
            log.info("ResponseData: "+rd.toString());
            return new ResponseEntity<>(rd, HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
