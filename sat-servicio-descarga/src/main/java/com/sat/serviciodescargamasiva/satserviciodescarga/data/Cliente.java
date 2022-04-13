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
    private boolean cuentaConKey;
    
    @Override
    public String toString() {
        return "[ Rfc: "+this.rfc+" Nombre: "+this.nombre+" apPaterno "+this.apPaterno+
                " ApMaterno "+this.apMaterno+" cuentaConContrasena "+this.cuentaConContrasena+
                " cuentaConCertificado "+this.cuentaConCertificado+" cuentaConKey "+this.cuentaConKey+"]";
    }
}
