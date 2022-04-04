/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.ResultadoVerificacion;
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
public interface VerifyRequest {
    void generate(X509Certificate certificate, PrivateKey privateKey, String idRequest, String rfcSolicitante)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException;
    
    ResultadoVerificacion verifyRequest(String token, String idRequest);
    
    ResultadoVerificacion doVerify(X509Certificate certificate, PrivateKey privateKey, String idRequest, 
            String rfcSolicitante, String token)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException;
            
}
