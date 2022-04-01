package com.sat.serviciodescargamasiva.satclientes;

import com.sat.serviciodescargamasiva.satclientes.data.ClienteVista;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesCliente;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
//import com.sat.serviciodescargamasiva.satclientes.jpa.ClienteVistaJpa;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class SatClientesApplicationTests {
    @Autowired
    private OperacionesCliente opCliente;
    
    @Test
    void contextLoads() {}
        
    @Test
    void testSaveCertificado() throws FileNotFoundException, IOException {
        File file = new File("C:\\Users\\elda_\\Desktop\\SAT\\SAT-WS-SQL.sql");
        FileInputStream fis = new FileInputStream(file);
        byte[] arr = new byte[(int)file.length()];
        fis.read(arr);
        fis.close();
        
        String uid = "W9uEjiv8s3QwddhpQVi6rbnyZ043";
        long idCliente = 7;
        ResponseData rd = opCliente.guardaCertificadoCliente(uid, idCliente, arr);
        assertTrue(rd.isOpValida());
        log.info("-------------------------");
        log.info("Resultado");
        log.info(rd.getMensaje());
    }
}
