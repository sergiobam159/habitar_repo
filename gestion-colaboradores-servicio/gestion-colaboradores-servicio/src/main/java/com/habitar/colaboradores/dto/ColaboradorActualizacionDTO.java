package com.habitar.colaboradores.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
	public class ColaboradorActualizacionDTO {

	    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
	    private String nombre;

	    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
	    private String apellido1;

	    @Size(min = 8, max = 15, message = "El DNI debe tener entre 8 y 15 caracteres")
	    private String dni;

	    @Email(message = "El formato del email no es válido")
	    private String email;

	    @Size(min = 2, max = 100, message = "La profesión debe tener entre 2 y 100 caracteres")
	    private String profesion;

	    @PastOrPresent(message = "La fecha de contratación no puede ser en el futuro")
	    private LocalDateTime fechaContratacion;

	    @Positive(message = "El sueldo debe ser un valor positivo")
	    private BigDecimal sueldo;
	
}
