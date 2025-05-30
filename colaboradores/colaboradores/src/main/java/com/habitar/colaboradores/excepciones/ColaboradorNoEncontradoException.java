package com.habitar.colaboradores.excepciones;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ColaboradorNoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L; //esto es por que está extendiendo de RUntimeException que es serializable, 1L es un valor equis long
	//Se le pone un valor long cualquiera, esto luego podría gestionarse
	//en caso de que requiera que cambie esta clase
	
	public ColaboradorNoEncontradoException(String id) {
        super("No se encontró el colaborador con ID: " + id);
    }
}
