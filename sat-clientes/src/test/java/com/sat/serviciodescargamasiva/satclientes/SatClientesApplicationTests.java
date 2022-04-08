package com.sat.serviciodescargamasiva.satclientes;

import com.sat.serviciodescargamasiva.satclientes.data.ClienteVista;
import com.sat.serviciodescargamasiva.satclientes.data.ResponseData;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesCliente;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesMedioContacto;
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
    OperacionesMedioContacto medioRepo;
    
    @Test
    void contextLoads() {}
    
    @Test
    void loadMedioCliente() {
        Object object = medioRepo.cargaListaMedioContactoCliente(2L);
        log.info("Medio de contacto: "+object);
    }
}
