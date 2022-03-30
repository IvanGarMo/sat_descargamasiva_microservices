package com.sat.serviciodescargamasiva.satusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.sat.serviciodescargamasiva.satusuarios.jpa")
public class SatUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SatUsuariosApplication.class, args);
	}

}
