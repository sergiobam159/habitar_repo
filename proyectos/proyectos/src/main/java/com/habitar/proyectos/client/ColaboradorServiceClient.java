package com.habitar.proyectos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.habitar.proyectos.dto.ColaboradorDTO; //Esto podemos luego gestionarlo con una biblioteca compartida

@FeignClient(name="colaboradores-servicio") 
//aqui dice que es cliente de el ms colaboradores xd
//Esto lo cambio cuando despliegue
public interface ColaboradorServiceClient {

	@GetMapping("/colaboradores/{id}")
    ResponseEntity<ColaboradorDTO> obtenerColaboradorPorId(@PathVariable String id);

}
