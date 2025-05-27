package com.habitar.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguracion {

	//RouteLocator es la interfaz principal de springt cloud gateway - esta encuentra una ruta para los id
	
	 @Bean
	     RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
	        return builder.routes()
	                // Ruta para el ms autenticacion
	                .route("autenticacion_route", r -> r.path("/api/auth/**") 
	                        .uri("lb://AUTENTICACION-SERVICIO")) 
	                // Ruta para el ms  colaboradores
	                .route("colaboradores_route", r -> r.path("/api/colaboradores/**")
	                        .filters(f -> f.rewritePath("/api/colaboradores/(?<segment>.*)", "/colaboradores/${segment}")) // Agrega este filtro
	                        .uri("lb://COLABORADORES-SERVICIO"))
	                // Ruta para el ms  proyectos
	                .route("proyectos_route", r -> r.path("/api/proyectos/**")
	                        .uri("lb://PROYECTOS-SERVICIO"))
	                // Ruta para el ms documentos
	                .route("documentos_route", r -> r.path("/api/documentos/**")
	                        .uri("lb://DOCUMENTOS-SERVICIO"))
	                .build();
	    }
	
}
