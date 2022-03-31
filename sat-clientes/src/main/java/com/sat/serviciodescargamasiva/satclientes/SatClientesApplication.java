package com.sat.serviciodescargamasiva.satclientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories("com.sat.serviciodescargamasiva.satclientes.jpa")
public class SatClientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SatClientesApplication.class, args);
	}

}
