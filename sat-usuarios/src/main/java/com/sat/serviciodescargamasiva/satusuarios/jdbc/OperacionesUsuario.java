/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.jdbc;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
import com.sat.serviciodescargamasiva.satusuarios.data.Usuario;

/**
 *
 * @author IvanGarMo
 */
public interface OperacionesUsuario {
    ResponseData registraUsuario(Usuario u);
    ResponseData actualizaUsuario(Usuario u);
    Usuario cargaUsuario(String uuid);
    int getIdUsuario(String uuid);
}
