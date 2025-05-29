package com.habitar.documentos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoSubidaDTO {
	 

    @NotBlank(message = "El id del proyecto no puede estar vacío")
     @Size(max = 255, message = "El ID del proyecto no puede exceder los 255 caracteres")
    private String idProyecto; 

    @Size(max = 255, message = "El nombre del archivo no puede exceder los 255 caracteres")
    private String nombreArchivoOriginal; 

    @Size(max = 255, message = "El id del usuario que sube no puede exceder los 255 caracteres")
    private String subidoPor; 

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String descripcion; 
}
