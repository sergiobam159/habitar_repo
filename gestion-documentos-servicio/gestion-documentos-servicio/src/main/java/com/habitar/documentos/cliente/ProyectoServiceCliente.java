package com.habitar.documentos.cliente;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.habitar.documentos.dto.ProyectoDTO;



@FeignClient(name="proyectos",url = "http://localhost:8080") 
public interface ProyectoServiceCliente {
	
	@GetMapping("/proyectos/{id}")
	ResponseEntity<Optional<ProyectoDTO>> obtenerProyectoPorId(@PathVariable String id);
	
	
	@GetMapping("/proyectos/checkExistencia/{id}")
	ResponseEntity<Boolean> checkProyectoExiste(@PathVariable String id);
}
