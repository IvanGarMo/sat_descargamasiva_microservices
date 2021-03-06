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
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author IvanGarMo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="usuariostelefonos")
public class Telefono {
    @Column(name="idusuario")
    private long idUsuario;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="idtelefono")
    private long idTelefono;
    
    @Size(min=0, max=13, message="El campo 'Teléfono debe tener entre 0 y 13 caracteres'")
    private String telefono;
    
    @Override
    public String toString() {
        return "[ IdUsuario: "+this.idUsuario+" IdTelefono: "+this.idTelefono+" Telefono: "+this.telefono+" ]";
    }
}
