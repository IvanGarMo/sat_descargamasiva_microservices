package com.sat.serviciodescargamasiva.satserviciodescarga;

import com.sat.serviciodescargamasiva.satserviciodescarga.servicios.AuthenticationProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class SatServicioDescargaApplicationTests {
    @Autowired
    private AuthenticationProvider authProvider;
    
    @Test
    void contextLoads() {}

    @Test
    void testTokenAutorizacion() throws KeyStoreException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, 
            CertificateEncodingException, UnsupportedEncodingException, IOException, CertificateException,
            UnrecoverableKeyException {
        File file = new File("C:\\Users\\Han-S\\Downloads\\FIEL_MONE711201Q44_20180912085358\\Keys\\csd.pfx");
        FileInputStream inputStream = new FileInputStream(file);
        char privateKey[] = "Gami9809".toCharArray();
        String token = authProvider.doAuthentication(inputStream, privateKey);
        log.info("testTokenAutorizacion");
        log.info("-----------------------------------");
        log.info("Token: "+token);
    }
}
