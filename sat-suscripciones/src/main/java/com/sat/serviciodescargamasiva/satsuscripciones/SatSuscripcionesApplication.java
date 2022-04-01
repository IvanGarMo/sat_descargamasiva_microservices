package com.sat.serviciodescargamasiva.satsuscripciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.sat.serviciodescargamasiva.satsuscripciones.jpa")
public class SatSuscripcionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SatSuscripcionesApplication.class, args);
	}

}
