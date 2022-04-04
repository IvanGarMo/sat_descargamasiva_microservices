/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.resultados.Resultado;
import java.io.IOException;

/**
 *
 * @author IvanGarMo
 */
public interface Download {

    void generate(X509Certificate certificate, PrivateKey privateKey, String rfcSolicitante, String idPackage)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException;

    Resultado getResult(String xmlResponse)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException;

    Resultado doDownload(X509Certificate certificate, PrivateKey privateKey,
            String rfc, String idPaquete, String token)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException, IOException;
}
