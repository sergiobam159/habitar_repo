package com.habitar.colaboradores.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
public class ColaboradorCreacionDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellido1;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 15, message = "El DNI debe tener entre 8 y 15 caracteres")
    private String dni;

    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La profesión es obligatoria")
    @Size(min = 2, max = 100, message = "La profesión debe tener entre 2 y 100 caracteres")
    private String profesion;

    @NotNull(message = "La fecha de contratación es obligatoria")
    @PastOrPresent(message = "La fecha de contratación no puede ser en el futuro")
    private LocalDateTime fechaContratacion;

    @NotNull(message = "El sueldo es obligatorio")
    @Positive(message = "El sueldo debe ser un valor positivo")
    private BigDecimal sueldo;
}