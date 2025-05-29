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

		// Genera una clave segura para HS256 (256 bits / 32 bytes)
		Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

		// clave en Base64 
		String secretString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		System.out.println("clave secreta segura: " + secretString);
		SpringApplication.run(AutenticacionApplication.class, args);
	}

}
