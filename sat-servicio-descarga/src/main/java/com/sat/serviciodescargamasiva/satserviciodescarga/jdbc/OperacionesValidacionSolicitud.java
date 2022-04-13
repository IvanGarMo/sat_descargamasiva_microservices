/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.Solicitud;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
import java.text.ParseException;
/**
 *
 * @author IvanGarMo
 */
public interface OperacionesValidacionSolicitud {
    ResponseData esValidaSolicitud(Solicitud solicitud) throws ParseException, JsonProcessingException;
}
