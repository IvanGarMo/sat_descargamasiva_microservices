/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.jdbc;

import com.sat.serviciodescargamasiva.satserviciodescarga.data.ContrasenaCertificado;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.ResponseData;
import com.sat.serviciodescargamasiva.satserviciodescarga.data.Solicitud;
import java.io.ByteArrayInputStream;

/**
 *
 * @author IvanGarMo
 */
public interface OperacionesAutorizacion {
    boolean tieneAccesoCliente(long idCliente, String uidFirebase);
    ResponseData tieneContrasena(long idCliente);
    ResponseData tieneCertificado(long idCliente);
    
    String cargaContrasena(long idCliente);
    ByteArrayInputStream cargaCertificadoCliente(long idCliente);
    ResponseData puedeHacerSolicitud(Solicitud solicitud);
    
    boolean tieneAccesoSolicitud(long idDescarga, String uidUser);
    ResponseData existeSolicitud(Solicitud solicitud);
}
