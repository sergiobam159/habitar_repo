package com.habitar.proyectos.dto;

import java.time.LocalDateTime; //mas moderno y mejor manejo que Date

import lombok.Data;

@Data
public class ProyectoDTO {

	 private String id;
	 private String nombre;
	 private String descripcion;
	 private String estado;
	 private String clienteId;
	 private String colaboradorResponsableId; //id obtenido desde el servicio de controlador
	 private LocalDateTime fechaDeInicio;
	 private String mensaje; 
	 
}
