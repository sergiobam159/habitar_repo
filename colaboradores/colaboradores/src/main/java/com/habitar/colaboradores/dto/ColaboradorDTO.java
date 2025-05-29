package com.habitar.colaboradores.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColaboradorDTO { //no le pongo verificadores a colaboradorDTO por que es solamente para utilizarlo como un objeto para la respuesta

    private String id;
    private String nombre;
    private String apellido1;
    private String dni;
    private String email;
    private String profesion;
    private LocalDateTime fechaContratacion;
    private BigDecimal sueldo;
}