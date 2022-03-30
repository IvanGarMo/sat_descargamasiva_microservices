/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.data;

import lombok.Data;

/**
 *
 * @author elda_
 */
@Data
public class Cliente {
    private long idCliente;
    private String rfc;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private boolean cuentaConContrasena;
    private String contrasena;
    private boolean cuentaConCertificado;
    private byte[] certificado;
}
