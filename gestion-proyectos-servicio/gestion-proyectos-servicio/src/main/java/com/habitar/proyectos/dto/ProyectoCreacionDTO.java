package com.habitar.proyectos.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank; // estas validaciones vienen desde la dependencia de "spring-boot-starter-validation" 
												//que utiliza Jakarta (estandar) para las anotaciones e Hibernate(implkementacion) es el validador
import lombok.Data;

@Data
public class ProyectoCreacionDTO {

	 @NotBlank(message = "El nombre del proyecto es obligatorio")
	    private String nombre;
	    private String descripcion;
	    private String estado;
	    @NotBlank(message = "El ID del cliente es obligatorio")
	    private String clienteId;
	    @NotBlank(message = "El ID del colaborador responsable es obligatorio")
	    private String colaboradorResponsableId;
	    private Date fechaDeInicio;
	
}
