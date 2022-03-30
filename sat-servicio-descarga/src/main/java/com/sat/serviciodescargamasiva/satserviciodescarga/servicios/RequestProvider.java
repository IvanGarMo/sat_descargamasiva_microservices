/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import java.security.cert.X509Certificate;

/**
 *
 * @author elda_
 */
public interface RequestProvider {
    String generate(X509Certificate certificate, String rfcSolicitante, String rfcEmisor, String rfcReceptor, 
            String fechaInicio, String fechaFinal, TipoSolicitud tipo);
            
}
