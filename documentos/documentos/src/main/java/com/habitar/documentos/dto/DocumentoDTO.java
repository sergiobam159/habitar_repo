
package com.habitar.documentos.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class DocumentoDTO {

    private Long id; 
    private String nombreArchivoOriginal;
    private String tipoContenido;
    private Long tamanoArchivo; 
    private String idBlobStorage; 
    private String urlAlmacenamiento; 
    private String idProyecto; 
    private LocalDateTime fechaSubida;
    private String subidoPor; 
    private String descripcion;
    private Boolean indHabilitado; 
}
