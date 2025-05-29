package com.habitar.documentos.controlador;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.habitar.documentos.Servicio.DocumentoServicio;
import com.habitar.documentos.dto.DocumentoDTO;
import com.habitar.documentos.dto.DocumentoSubidaDTO;

@Controller
@RequestMapping("/documentos")
public class DocumentoController {
	
	 private final DocumentoServicio documentoServicio;
	 
	 public DocumentoController(DocumentoServicio documentoServicio) {
		this.documentoServicio= documentoServicio; 
	 }
	
	
	 @PostMapping("/subir")
		public ResponseEntity<DocumentoDTO> subirDocumento(@RequestParam MultipartFile file,@RequestParam String idProyecto,
	            @RequestParam(required = false) String nombreArchivoOriginal,
	            @RequestParam String subidoPor,
	            @RequestParam String descripcion) {
			
	        DocumentoSubidaDTO datosSubida = new DocumentoSubidaDTO();
	        datosSubida.setIdProyecto(idProyecto);
	        datosSubida.setNombreArchivoOriginal(nombreArchivoOriginal != null ? nombreArchivoOriginal : file.getOriginalFilename());
	        datosSubida.setSubidoPor(subidoPor);
	        datosSubida.setDescripcion(descripcion);
			
	       
	        DocumentoDTO nuevoDocumento = documentoServicio.subirDocumento(file, datosSubida);
	        
	        return new ResponseEntity<>(nuevoDocumento, HttpStatus.CREATED);
		}
	
	 @GetMapping("/descargar/{idDocumento}")
	    public ResponseEntity<ByteArrayResource> descargarDocumento(@PathVariable Long idDocumento) {
		 
		 //aqui se van a meter los datos binarios del documento desde el servicio que lo descarga
	        byte[] data = documentoServicio.descargarDocumento(idDocumento);
	        String nombreDocumento =  documentoServicio.obtenerDocumentoPorId(idDocumento).get().getNombreArchivoOriginal();
	        //aqui configuramos las cabeceras para la respuesta de retorno (Es ResponseEntity, que tiene body y cabecera
	        
	        //headers
	        //content_dispositio: e lattachment es para que el cte sepa que es descargable 
	        //y filename para el nombre
	        //Contenttype: tipo de contenido del archivo
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"documento_" + nombreDocumento + "\"");
	        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
	        
	        
	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(data.length)
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(new ByteArrayResource(data));
	    }
	 
	 @GetMapping("/proyecto/{idProyecto}")
	    public ResponseEntity<List<DocumentoDTO>> listarDocumentosPorProyecto(@PathVariable String idProyecto) {
	        List<DocumentoDTO> documentos = documentoServicio.listarDocumentosPorProyecto(idProyecto);
	        return new ResponseEntity<>(documentos, HttpStatus.OK);
	    }

	 
	 @GetMapping("/{id}")
	    public ResponseEntity<DocumentoDTO> obtenerDocumentoPorId(@PathVariable Long id) {
	        return documentoServicio.obtenerDocumentoPorId(id)
	                .map(documentoDTO -> new ResponseEntity<>(documentoDTO, HttpStatus.OK))
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado o no habilitado con ID: " + id));
	    }
	 
	 @PutMapping("/deshabilitar/{id}")
	    public ResponseEntity<Void> deshabilitarDocumento(@PathVariable Long id) {
	        documentoServicio.deshabilitarDocumento(id);
	        return new ResponseEntity<>(HttpStatus.OK);
	    }
	  @PutMapping("/habilitar/{id}")
	    public ResponseEntity<Void> habilitarDocumento(@PathVariable Long id) {
	        documentoServicio.habilitarDocumento(id);
	        return new ResponseEntity<>(HttpStatus.OK);
	    }
	  
	  
	 @DeleteMapping("/blob/{idBlobStorage}")
	    public ResponseEntity<Void> eliminarDocumento(@PathVariable String idBlobStorage) {
	        documentoServicio.eliminarDocumento(idBlobStorage);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	 
	 
	
}

