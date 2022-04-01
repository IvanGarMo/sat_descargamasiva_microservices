/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satsuscripciones.jdbc;

import com.sat.serviciodescargamasiva.satsuscripciones.data.ResponseData;
import com.sat.serviciodescargamasiva.satsuscripciones.data.Suscripcion;

/**
 *
 * @author IvanGarMo
 */
public interface OperacionesSuscripciones {
    int getSuscripcionPorUsuario(String uuid);
    ResponseData cambiaSuscripcionUsuario(String uuid, int idSuscripcion);
}
