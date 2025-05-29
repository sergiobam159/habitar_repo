package com.habitar.colaboradores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class ColaboradoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColaboradoresApplication.class, args);
	}

}
