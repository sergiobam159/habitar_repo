package com.habitar.documentos.Servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.habitar.documentos.dto.DocumentoDTO;

public interface DocumentoServicio {
	
	// recibe el archivoy el id del proyecto al que pertenece
	 DocumentoDTO subirDocumento(MultipartFile file, String idProyecto);

	    // descarga un documento por su id del blob en Azure.
	    byte[] descargarDocumento(String idBlobStorage);

	    //  elimina un documento por su id del blob en Azure.
	    //  metadato en la bd  y el archivo en Azure borra ambos
	    void eliminarDocumento(String idBlobStorage);

	    //  listar todos los documentos de un proyecto específico.
	   
	    List<DocumentoDTO> listarDocumentosPorProyecto(String idProyecto);

	    // obtener los metadatos de un solo documento por su id de base de datos.
	    Optional<DocumentoDTO> obtenerDocumentoPorId(Long id);

	    
	    void deshabilitarDocumento(Long id);

	    
	    void habilitarDocumento(Long id);
}
