/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.data;

import lombok.Data;

/**
 *
 * @author IvanGarMo
 */
@Data
public class Cliente {
    private String rfc;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private boolean cuentaConContrasena;
    private boolean cuentaConCertificado;
}
