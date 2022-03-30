/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author IvanGarMo
 */
@Entity
@Table(name="Usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long idUsuario;
    @Column(name="UidUserFirebase")
    private String uid;
    @Column(name="Nombre")
    private String nombre;
    @Column(name="ApPaterno")
    private String apPaterno;
    @Column(name="ApMaterno")
    private String apMaterno;
    @Column(name="Correo")
    private String correo;
    @Column(name="IdSuscripcion")
    private int idSuscripcion;
    @Column(name="Organizacion")
    private String organizacion;
    @Column(name="Activo")
    private boolean activo;
}
