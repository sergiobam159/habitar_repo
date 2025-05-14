package com.habitar.colaboradores.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection="colaboradores")
public class Colaborador {

	@Id
	private String id;
	@Field("nombre")
    private String nombre;
	@Field("apellido1")
	private String apellido1;
	@Field("dni")
	private String dni;
	@Field("email")
    private String email;
	@Field("profesion")
    private String profesion;
	@Field("fechaContratacion")
    private LocalDateTime fechaContratacion;
	@Field("sueldo")
    private BigDecimal sueldo;
}
