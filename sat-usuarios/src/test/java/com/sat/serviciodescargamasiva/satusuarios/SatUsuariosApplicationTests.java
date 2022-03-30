package com.sat.serviciodescargamasiva.satusuarios;

import com.sat.serviciodescargamasiva.satusuarios.data.Telefono;
import com.sat.serviciodescargamasiva.satusuarios.jdbc.OperacionesUsuario;
import com.sat.serviciodescargamasiva.satusuarios.jpa.JpaTelefono;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class SatUsuariosApplicationTests {
        @Autowired
        private JpaTelefono jpa;
        @Autowired
        private OperacionesUsuario operacionesUsuario;
    
	@Test
	void contextLoads() {
	}
        
        @Test
        void leeId() {
            log.info("Lee id: "+operacionesUsuario.getIdUsuario("BpwOa6zUAFTbJe3qD4LjjvddaPq2"));
        }
        
        @Test
        void leeTelefonos() {
            List<Telefono> telefonos = jpa.findByIdUsuario(1L);
            log.info("------------------------------------------------");
            telefonos.forEach(t -> log.info(t.toString()));
        }

}
