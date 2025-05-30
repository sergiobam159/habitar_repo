package com.habitar.autenticacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Base64;

@EnableDiscoveryClient
@SpringBootApplication
public class AutenticacionApplication {

	public static void main(String[] args) {

		
		SpringApplication.run(AutenticacionApplication.class, args);
	}

}
