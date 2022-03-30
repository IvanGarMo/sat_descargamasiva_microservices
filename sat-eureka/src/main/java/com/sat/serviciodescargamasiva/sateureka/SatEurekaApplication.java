package com.sat.serviciodescargamasiva.sateureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer
public class SatEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SatEurekaApplication.class, args);
	}

}
