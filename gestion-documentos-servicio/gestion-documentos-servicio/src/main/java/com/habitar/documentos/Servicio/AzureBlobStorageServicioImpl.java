package com.habitar.documentos.Servicio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;

@Service
public class AzureBlobStorageServicioImpl implements AzureBlobStorageServicio{

	private final BlobContainerClient blobContainerClient; //contenedor de blobs

	
    public  AzureBlobStorageServicioImpl(BlobServiceClient blobServiceClient,@Value("${spring.cloud.azure.storage.blob.container-name}") String containerName) {
        this.blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
       
    }

	@Override
	public String subirArchivo(MultipartFile file) throws IOException {
		String blobId = UUID.randomUUID().toString(); //un identificador unico universal UUID 
		try {
		BlobClient blobClient = blobContainerClient.getBlobClient(blobId); 
        //ya este BlobClient es un cliente que trabaja con un UN Y SOLO UN archivo en el blob storage
        //al ponerle el blobId en el getblobclient se crea el cliente y genera el id de este nuevo blob
        //con el UUID generado
        
            blobClient.upload(file.getInputStream(), file.getSize(), true); //sube el archivo a través del cliente
            return blobId; //el id del blob que se guardará en la bd
            
        } catch (IOException e) {
            throw new IOException("Error al subir el archivo al blob storage: " + e.getMessage(), e);
        }
       
	}

	@Override
	public byte[] descargarArchivo(String blobId) throws IOException {////aqui se le pasa ya un blob id,
		 BlobClient blobClient = blobContainerClient.getBlobClient(blobId); //  crea un cliente que apunta a ese blobid

	        if (!blobClient.exists()) { //Este metodo exist verifica si hay algun blob con el id de este cliente. metodo es de azure
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo no encontrado en Azure Blob Storage con ID: " + blobId);
	        }
	        
	        //crea un bufer en memoria para la descarga
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        try {
	            blobClient.downloadStream(outputStream); //aqui descarga el archivo al que el clienteblob apunta
	            /* OJO AQUI
	             * cuando pasas un OBJETO a un METODO, lo que se pasa es una COPIA del valor de la REFERENCIA
	             * 
	             * osea, que cuando craemos outputStream este es una referencia a un espacio en memoria
	             * y cuando lo pasamos al donwnloadStream solo pasamos la referencia al mismo espacio en memoria
	             * por eso puede transformarlo 
	             */
	            //al outputsrtream
	            return outputStream.toByteArray(); //convierte el outputStream en un arreglo de bytes
	        } catch (Exception e) {  
	            throw new IOException("Error al descargar el archivo del blob storage: " + e.getMessage(), e);
	        }
	}

	@Override
	public void borrarArchivo(String blobId) throws IOException {
		 BlobClient blobClient = blobContainerClient.getBlobClient(blobId);

	        if (!blobClient.exists()) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo no encontrado en Azure Blob Storage para eliminar con ID: " + blobId);
	        }

	        try {
	            blobClient.delete();
	        } catch (Exception e) {
	            throw new IOException("Error al eliminar el archivo del blob storage: " + e.getMessage(), e);
	        }
		
	}

	@Override
	public String obtenerUrl(String blobId) {
		 BlobClient blobClient = blobContainerClient.getBlobClient(blobId);
	        return blobClient.getBlobUrl();
	    
	}
	
	

}
