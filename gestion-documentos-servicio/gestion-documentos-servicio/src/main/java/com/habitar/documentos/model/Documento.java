package com.habitar.documentos.model;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="documentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Documento {

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY) // identity para que aumente al creaar
	  @Column(name="id_archivo", length =255)
	    private Long id; // ID primario de la base de datos

	    @Column(name = "nombre_archivo_original", length = 255) 
	    private String nombreArchivoOriginal;
	
	    @Column(name = "tipo_contenido", length = 100)
	    private String tipoContenido;

	     @Column(name = "tamano_archivo")
	    private Long tamanoArchivo;

	   @Column(name = "id_blob_storage", length = 255, unique = true) 
	    private String idBlobStorage; // aca el id del documento en blob de azure 

	    @Column(name = "url_almacenamiento")
	    private String urlAlmacenamiento; 

	    @Column(name = "id_proyecto", length = 255)
	    private String idProyecto;

	    @Column(name = "fecha_subida")
	    private LocalDateTime fechaSubida;

	    @Column(name = "subido_por", length = 255)
	    private String subidoPor; // id del colaboradorcito ito

	    @Column(name = "descripcion")
	    private String descripcion; 
	    
	    @Column(name = "ind_habilitado", nullable = false) 
	    private Boolean indHabilitado; // true habilitado, false dehabilitado

}
