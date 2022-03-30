package com.sat.serviciodescargamasiva.satconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class SatConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SatConfigServerApplication.class, args);
	}

}
