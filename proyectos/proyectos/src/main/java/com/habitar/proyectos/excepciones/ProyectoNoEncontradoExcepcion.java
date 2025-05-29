package com.habitar.proyectos.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProyectoNoEncontradoExcepcion extends RuntimeException {
	
	private static final long serialVersionUID = 1L; //esto es por que está extendiendo de RUntimeException 
	//que es serializable, 1L es un valor cualquiera tipo long
	
	public ProyectoNoEncontradoExcepcion(String id) {
        super("No se encontró el proyecto con ID: " + id);
    }
	
}
