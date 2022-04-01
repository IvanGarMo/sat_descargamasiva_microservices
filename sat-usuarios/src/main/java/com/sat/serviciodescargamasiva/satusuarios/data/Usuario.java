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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    
    @NotNull(message="Debe incluir un nombre")
    @Size(min=0, max=100, message="El campo 'Nombre' debe tener entre 0 y 100 caracteres")
    private String nombre;
    
    @NotNull(message="Debe incluir su apellido paterno")
    @Size(min=0, max=100, message="El campo 'Apellido Paterno' debe tener entre 0 y 100 caracteres")
    private String apPaterno;
    
    @NotNull(message="Debe incluir su apellido materno")
    @Size(min=0, max=100, message="El campo 'Apellido Materno' debe tener entre 0 y 100 caracteres")
    private String apMaterno;
    
    @NotNull(message="Debe incluir su correo")
    @Size(min=0, max=100, message="El campo 'Correo' debe tener entre 0 y 100 caracteres")
    private String correo;
    
    @NotNull(message="Debe seleccionar una suscripción")
    private int idSuscripcion;
    
    @NotNull(message="Debe seleccionar una organización")
    @Size(min=0, max=100, message="El campo 'Organización' debe tener entre 0 y 100 caracteres")
    private String organizacion;
    
    @NotNull(message="Debe indicar si el usuario está activo o no")
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
