package com.habitar.proyectos.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProyectoActualizacionDTO {
	 @NotBlank(message = "El ID del proyecto a actualizar es obligatorio")
	    private String id;
	    private String nombre;
	    private String descripcion;
	    private String estado;
	    private String clienteId;
	    private String colaboradorResponsableId;
	    private Date fechaDeInicio;
}
