/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 *
 * @author elda_
 */
public interface AuthenticationProvider {
    X509Certificate getCertificate(File file) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException;
    PrivateKey getPrivateKey(File file) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException;
    void generate(X509Certificate certificate, PrivateKey privateKey);
    String getToken();
}
