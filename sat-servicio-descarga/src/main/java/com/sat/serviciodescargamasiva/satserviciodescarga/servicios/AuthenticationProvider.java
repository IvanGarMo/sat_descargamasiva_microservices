/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sat.serviciodescargamasiva.satserviciodescarga.servicios;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 *
 * @author elda_
 */
public interface AuthenticationProvider {
    X509Certificate getCertificate(InputStream file, char[] key) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException;
    PrivateKey getPrivateKey(InputStream file, char[] key) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException;
    void generate(X509Certificate certificate, PrivateKey privateKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException;
    String getToken() throws IOException;
    String doAuthentication(InputStream file, char[] privateKey) 
            throws KeyStoreException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, 
            CertificateEncodingException, UnsupportedEncodingException, IOException, CertificateException,
            UnrecoverableKeyException;
    }
