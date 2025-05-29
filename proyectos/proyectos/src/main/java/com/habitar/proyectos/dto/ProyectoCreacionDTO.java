package com.habitar.proyectos.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank; // estas validaciones vienen desde la dependencia de "spring-boot-starter-validation" 
import jakarta.validation.constraints.Size; 
//que utiliza Jakarta bean validation -  para las anotaciones validadoras e spring data mongodb como ODM

//ODM para bases de datos no relacionales (documentos)
//para bd relacionales es ORM (relacional) (JPA)

import lombok.Data;

@Data
public class ProyectoCreacionDTO {

	 @NotBlank(message = "El nombre del proyecto es obligatorio")
	 @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
	    private String nombre;
	    private String descripcion;
	    @NotBlank(message = "El estado es obligatorio")
	    private String estado;
	    @NotBlank(message = "El id del cliente es obligatorio")
	    private String clienteId;
	    @NotBlank(message = "El id del colaborador responsable es obligatorio")
	    private String colaboradorResponsableId;
	    private LocalDateTime fechaDeInicio;
	
}
