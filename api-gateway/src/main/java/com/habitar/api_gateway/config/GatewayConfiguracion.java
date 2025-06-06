package com.habitar.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import org.springframework.context.annotation.Configuration;
import com.habitar.api_gateway.filtro.FiltroAutorizacion;


@Configuration
public class GatewayConfiguracion {

    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder, FiltroAutorizacion filtroAutorizacion) {
    	return builder.routes()
                // Ruta para el ms autenticacion
                .route("autenticacion_route", r -> r.path("/api/auth/**")
                        .filters(f -> f.rewritePath("/api/auth(?<segment>.*)", "/auth${segment}")) // <-- Ajustado aquí
                        .uri("lb://AUTENTICACION-SERVICIO"))

                // Ruta para el ms colaboradores
                .route("colaboradores_route", r -> r.path("/api/colaboradores/**")
                        .filters(f -> f
                                .filter(filtroAutorizacion)
                                .rewritePath("/api/colaboradores(?<segment>.*)", "/colaboradores${segment}")) // <-- Ajustado aquí
                        .uri("lb://COLABORADORES-SERVICIO"))

                // Ruta para el ms proyectos
                .route("proyectos_route", r -> r.path("/api/proyectos/**")
                        .filters(f -> f
                                .filter(filtroAutorizacion)
                                .rewritePath("/api/proyectos(?<segment>.*)", "/proyectos${segment}")) // <-- Ajustado aquí
                        .uri("lb://PROYECTOS-SERVICIO"))

                // Ruta para el ms documentos
                .route("documentos_route", r -> r.path("/api/documentos/**")
                        .filters(f -> f
                                .filter(filtroAutorizacion)
                                .rewritePath("/api/documentos(?<segment>.*)", "/documentos${segment}")) // <-- Ajustado aquí
                        .uri("lb://DOCUMENTOS-SERVICIO"))
                .build();
    }
}