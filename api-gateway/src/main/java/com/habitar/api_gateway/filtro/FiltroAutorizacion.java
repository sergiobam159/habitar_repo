package com.habitar.api_gateway.filtro;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


public class FiltroAutorizacion implements GatewayFilter{
	 @Value("${jwt.secret}")
	    private String secret;

	    private Key getSigningKey() {
	        byte[] keyBytes = Decoders.BASE64.decode(secret);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }

	 //valido el token 
	    public Claims validarToken(String token) {
	        try {
	            return Jwts.parserBuilder()
	                    .setSigningKey(getSigningKey()) // Usa la clave secreta para verificar la firma
	                    .build()
	                    .parseClaimsJws(token) //aqui valida eltoken con la clave secret
	                    .getBody(); //si va bien retorna el body del token- que es un objeto Claims
	        } catch (Exception e) {
	            // loggea el error
	            System.err.println("erroe validando el JWT: " + e.getMessage()); 
	            throw new RuntimeException("JWT es invalido o ya no sirve: " + e.getMessage());
	        }
	    }

	    @Override
	    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
	        ServerHttpRequest request = exchange.getRequest();

	        // Rutas que NO requieren autenticación 
	        //  la ruta de login no es interceptada
	        
	        if (request.getURI().getPath().startsWith("/auth/login") ) {
	            return chain.filter(exchange);
	        } //aqui permite que la peticion de ese endpoint siga sin filtrar

	        // extrae el token del header authorization
	        String authHeader = request.getHeaders().getFirst("Authorization");

	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            return onError(exchange, HttpStatus.UNAUTHORIZED, "header authorization invalido");
	        }

	        String token = authHeader.substring(7); // quita "Bearer" (7 caracteres)

	        try {
	            // valida el token y obtener los claims
	            Claims claims = validarToken(token);

	            // 3. Opcional: Añadir información del usuario a los headers de la petición para los microservicios
	            // Esto evita que los microservicios tengan que re-validar el token.
	            ServerHttpRequest mutatedRequest = request.mutate()
	                    .header("usuarioLogado", claims.getSubject())
	                    .build();
	            exchange = exchange.mutate().request(mutatedRequest).build();

	        } catch (RuntimeException e) {
	            // si falla mandamos error de validación del token
	            return onError(exchange, HttpStatus.UNAUTHORIZED, e.getMessage());
	        }

	        // si todo sale ok sigue el camino al microservicio
	        return chain.filter(exchange);
	    }

	    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus, String errorMessage) {
	        ServerHttpResponse response = exchange.getResponse();
	        response.setStatusCode(httpStatus);
	  
	        return response.setComplete(); 
	    }
}
