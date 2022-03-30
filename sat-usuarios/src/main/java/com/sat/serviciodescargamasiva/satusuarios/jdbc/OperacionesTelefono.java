/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satusuarios.jdbc;

import com.sat.serviciodescargamasiva.satusuarios.data.ResponseData;
import com.sat.serviciodescargamasiva.satusuarios.data.Telefono;
import java.util.List;

/**
 *
 * @author IvanGarMo
 */
public interface OperacionesTelefono {
    ResponseData capturaTelefonoUsuario(String uuid, String telefono);
    ResponseData eliminaTelefonoUsuario(String uuid, long idTelefono);
    //List<Telefono> listaTelefonosUsuario(String uuid);
}
