package com.habitar.autenticacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Base64;

@SpringBootApplication
public class AutenticacionApplication {

	public static void main(String[] args) {

		// Genera una clave segura para HS256 (256 bits / 32 bytes)
		Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

		// Puedes codificar la clave en Base64 para guardarla en tu archivo de propiedades
		String secretString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		System.out.println("Tu nueva clave secreta segura (copia esto en tu application.properties): " + secretString);
		SpringApplication.run(AutenticacionApplication.class, args);
	}

}
