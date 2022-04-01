/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author IvanGarMo
 */
@Data
public class Cliente {
    private long idCliente;
    @NotNull(message="Debe incluir el campo 'Rfc'")
    @Size(min=13, max=13, message="El campo 'Rfc' debe tener 13 caracteres de longitud")
    private String rfc;
    
    @NotNull(message="Debe incluir el campo 'Nombre'")
    @Size(min=1, max=100, message="El campo 'Nombre' debe tener entre 1 y 100 caracteres de longitud")
    private String nombre;
    
    @NotNull(message="Debe incluir el campo 'Apellido paterno'")
    @Size(min=1, max=100, message="El campo 'Apellido paterno' debe tener entre 1 y 100 caracteres de longitud")
    private String apPaterno;

    @NotNull(message="Debe incluir el campo 'Apellido materno'")
    @Size(min=1, max=100, message="El campo 'Apellido materno' debe tener entre 1 y 100 caracteres de longitud")
    private String apMaterno;
    
    private boolean cuentaConContrasena;
    private String contrasena;
    
    private boolean cuentaConCertificado;
    private byte[] certificado;
}
