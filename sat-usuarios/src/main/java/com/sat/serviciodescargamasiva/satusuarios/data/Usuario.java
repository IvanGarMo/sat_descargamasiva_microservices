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
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long idUsuario;
    private String uid;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private String correo;
    private int idSuscripcion;
    private String organizacion;
    private boolean activo;
    
    public boolean getActivo() {
        return activo;
    }
    
    @Override
    public String toString() {
        return "[ uid:"+this.uid+" IdUsuario: "+this.idUsuario+" Nombre: "+this.nombre+
                " ApPaterno +"+this.apPaterno+"ApMaterno: "+this.apMaterno+
                " Correo: "+ this.correo + "Organizacion: "+this.organizacion +
                " Activo: "+this.activo
                + "]";
    }
}
