/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.jdbc;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.ContrasenaCertificado;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;

/**
 *
 * @author IvanGarMo
 */
public interface OperacionesSuscripcion {
    boolean tieneAccesoCliente(int idCliente, String uidFirebase);
    ResponseData tieneContrasena(int idCliente);
    ResponseData tieneCertificado(int idCliente);
    ContrasenaCertificado cargaDatosAutenticacion(int idCliente);
}
