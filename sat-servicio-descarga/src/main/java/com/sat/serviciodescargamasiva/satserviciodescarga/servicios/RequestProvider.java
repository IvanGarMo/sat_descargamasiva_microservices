/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.Resultado;
import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.ResultadoSolicitudDescarga;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/**
 *
 * @author IvanGarMo
 */
public interface RequestProvider {
    
    ResultadoSolicitudDescarga generate(X509Certificate certificate, PrivateKey privateKey,
            String rfcSolicitante, String rfcEmisor, String rfcReceptor,
            String fechaInicio, String fechaFinal, TipoSolicitud tipo)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException;

    void setType(TipoSolicitud tipo);

    String sendRequest(String token);

    ResultadoSolicitudDescarga doRequest(X509Certificate certificate, PrivateKey privateKey,
            String rfcSolicitante, String rfcEmisor, String rfcReceptor,
            String fechaInicio, String fechaFinal, TipoSolicitud tipo)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException;
    
    Resultado validate(String rfcSolicitante, String rfcEmisor, String rfcReceptor, 
            String fechaInicio, String fechaFinal, TipoSolicitud tipo)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException;
}
