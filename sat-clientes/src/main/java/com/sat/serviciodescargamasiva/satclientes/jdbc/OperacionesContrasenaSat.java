/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satclientes.jdbc;

import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;

/**
 *
 * @author IvanGarMo
 */
public interface OperacionesContrasenaSat {
    ResponseData guardaContrasenaCliente(String uidUsuario, long idCliente, String contrasena);
}
