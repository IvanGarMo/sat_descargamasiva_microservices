package com.sat.serviciodescargamasiva.satclientes;

import com.sat.serviciodescargamasiva.satclientes.data.ClienteVista;
import com.sat.serviciodescargamasiva.satclientes.jdbc.OperacionesCliente;
//import com.sat.serviciodescargamasiva.satclientes.jpa.ClienteVistaJpa;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SatClientesApplicationTests {
    @Autowired
    private OperacionesCliente opCliente;
    
    @Test
    void contextLoads() {}
        
    @Test
    void listaClientes() {
        opCliente.getClientes("BpwOa6zUAFTbJe3qD4LjjvddaPq2", "", "", "", "");        
    }
}
