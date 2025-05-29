package com.habitar.documentos.Servicio;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface AzureBlobStorageServicio {
	
		String subirArchivo(MultipartFile file) throws IOException;

	    byte[] descargarArchivo(String blobId) throws IOException;

	    void borrarArchivo(String blobId) throws IOException;

	    String obtenerUrl(String blobId);
}
