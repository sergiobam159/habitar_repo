package com.habitar.proyectos.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habitar.proyectos.dto.ProyectoCreacionDTO;
import com.habitar.proyectos.dto.ProyectoDTO;
import com.habitar.proyectos.servicio.ProyectoServicio;

@RestController
@RequestMapping("/proyectos")
public class Controlador {

	private final ProyectoServicio proyectoServicio;
	
	@Autowired
	public Controlador(ProyectoServicio proyectoServicio) {
		this.proyectoServicio = proyectoServicio;
	}
	
	
	@PostMapping
    public ResponseEntity<ProyectoDTO> crearProyecto(@RequestBody ProyectoCreacionDTO proyectoCreacionDTO) {
        ProyectoDTO proyectoGuardado = proyectoServicio.crearProyecto(proyectoCreacionDTO);
        return new ResponseEntity<>(proyectoGuardado, HttpStatus.CREATED);
    }
	
}
