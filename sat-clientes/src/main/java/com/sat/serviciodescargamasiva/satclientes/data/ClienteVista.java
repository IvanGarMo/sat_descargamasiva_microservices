/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author IvanGarMo
 */
@Entity
@Table(name="UsuariosCliente")
@Getter
@Setter
public class ClienteVista {
    @Id
    private long idCliente;
    private String Rfc;
    private long idUsuario;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private boolean cuentaConContrasena;
    private boolean cuentaConCertificado;
    
    @Override
    public String toString() {
        return "[ IdCliente: "+this.idCliente+" Rfc: "+this.Rfc+" IdUsuario "+this.idUsuario+
                " Nombre: "+this.nombre+" ApPaterno "+this.apPaterno+" ApMaterno "+this.apMaterno+"]";
    }
}
