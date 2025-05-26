package com.habitar.documentos.Servicio;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.habitar.documentos.client.ProyectoServiceClient;
import com.habitar.documentos.dto.DocumentoDTO;
import com.habitar.documentos.dto.DocumentoSubidaDTO;
import com.habitar.documentos.model.Documento;
import com.habitar.documentos.repositorio.DocumentoRepositorio;

import jakarta.transaction.Transactional;

@Service
public class DocumentoServicioImpl implements DocumentoServicio {


    private final DocumentoRepositorio documentoRepositorio;
    private final AzureBlobStorageServicio azureBlobStorageServicio;
    private final ProyectoServiceClient proyectoServiceCliente;

    
    public DocumentoServicioImpl(DocumentoRepositorio documentoRepositorio,
    							AzureBlobStorageServicio azureBlobStorageServicio,
                                ProyectoServiceClient proyectoServiceCliente) {
        this.documentoRepositorio = documentoRepositorio;
        this.azureBlobStorageServicio = azureBlobStorageServicio;
        this.proyectoServiceCliente = proyectoServiceCliente;
    }

    @Override
    @Transactional
    public DocumentoDTO subirDocumento(MultipartFile file, DocumentoSubidaDTO documentoDTO) {
    	String idProyecto = documentoDTO.getIdProyecto();
    	 Optional.ofNullable(idProyecto) // ofnullable genera un optional
         .filter(id -> !id.isEmpty()) //filtro si no esta vacío empty y si ESTA VACIO thoweo la excepcion
         .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del proyecto no puede ser nulo o vacío."));

        try {
        	 if(!proyectoServiceCliente.checkProyectoExiste(idProyecto).getBody()) { //aqui llama al metodo checkProyectoExiste
        		 //del miroservicio proyectos, este retorna un booleano en el bodu del ResponseEntity
        		 //si no hay se lanza 404
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El proyecto con ID " + idProyecto + " no existe.");
        	 }
        } catch (Exception e) { //SI FALLA la petición se lanza 503 
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo verificar la existencia del proyecto. Servicio de Proyectos no disponible o error de comunicación.", e);
        }
//SI EL ID PROYECTO NO ES NULO Y EL PROYECTO EXISTE SE SUBE
        try {
            String idBlobStorage = azureBlobStorageServicio.subirArchivo(file); //al guardar retorna el  idblobstorage UUID creado
            String urlAlmacenamiento = azureBlobStorageServicio.obtenerUrl(idBlobStorage); //aqui usa el mismo servicio para obtener del la url del idblobstorage
//con el id del blob y la url generamos el insert en la bd (metodo .save de JPA)
            Documento documento = new Documento();
            documento.setNombreArchivoOriginal(file.getOriginalFilename());
            documento.setTipoContenido(file.getContentType());
            documento.setTamanoArchivo(file.getSize());
            documento.setIdBlobStorage(idBlobStorage);
            documento.setUrlAlmacenamiento(urlAlmacenamiento);
            documento.setIdProyecto(idProyecto);
            documento.setFechaSubida(LocalDateTime.now());
            documento.setIndHabilitado(true); //por defecto habilitado

            Documento savedDocumento = documentoRepositorio.save(documento); //esto retorna el documento
            return convertirDTO(savedDocumento); //lo retornamos como DTO

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar el archivo para subir.", e);
        }
    }

    @Override
    public byte[] descargarDocumento(Long idDocumento) {
        Documento documento = documentoRepositorio.findById(idDocumento)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado: " + idDocumento));

        if (!documento.getIndHabilitado()) {
            throw new ResponseStatusException(HttpStatus.GONE, "El documento con ID " + idDocumento + " no está habilitado para descarga.");
        }

        try {
            return azureBlobStorageServicio.descargarArchivo(documento.getIdBlobStorage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al descargar el archivo de Azure Blob Storage.", e);
        }
    }

    @Override
    @Transactional
    public void eliminarDocumento(String idBlobStorage) {
        Documento documento = documentoRepositorio.findByIdBlobStorage(idBlobStorage)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Metadatos de documento no encontrados para ID de blob: " + idBlobStorage));

        try {
            azureBlobStorageServicio.borrarArchivo(idBlobStorage);
            documentoRepositorio.delete(documento);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el archivo de Azure Blob Storage.", e);
        }
    }

    @Override
    public List<DocumentoDTO> listarDocumentosPorProyecto(String idProyecto) {
        if (idProyecto == null || idProyecto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del proyecto no puede ser nulo o vacío.");
        }

        try {
            if (!proyectoServiceCliente.checkProyectoExiste(idProyecto).getBody()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El proyecto con ID " + idProyecto + " no existe.");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo verificar la existencia del proyecto. Servicio de Proyectos no disponible o error de comunicación.", e);
        }

        
        return documentoRepositorio.findByIdProyecto(idProyecto).stream()
                .filter(document -> document.getIndHabilitado().equals(true))
                .map(docume -> this.convertirDTO(docume))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DocumentoDTO> obtenerDocumentoPorId(Long id) {
        return documentoRepositorio.findById(id)
        		.filter(document -> document.getIndHabilitado().equals(true)) // Solo devolvemos si está habilitado
                .map(docume -> this.convertirDTO(docume));
    }

    @Override
    @Transactional
    public void deshabilitarDocumento(Long id) {
        Documento documento = documentoRepositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado con ID: " + id));
        documento.setIndHabilitado(false);
        documentoRepositorio.save(documento); // Guarda la entidad actualizada
    }

    @Override
    @Transactional
    public void habilitarDocumento(Long id) {
        Documento documento = documentoRepositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento no encontrado con ID: " + id));
        documento.setIndHabilitado(true);
        documentoRepositorio.save(documento); // Guarda la entidad actualizada
    }

    private DocumentoDTO convertirDTO(Documento documento) {
        DocumentoDTO dto = new DocumentoDTO();
        dto.setId(documento.getId());
        dto.setNombreArchivoOriginal(documento.getNombreArchivoOriginal());
        dto.setTipoContenido(documento.getTipoContenido());
        dto.setTamanoArchivo(documento.getTamanoArchivo());
        dto.setIdBlobStorage(documento.getIdBlobStorage());
        dto.setUrlAlmacenamiento(documento.getUrlAlmacenamiento());
        dto.setIdProyecto(documento.getIdProyecto());
        dto.setFechaSubida(documento.getFechaSubida());
        dto.setSubidoPor(documento.getSubidoPor());
        dto.setDescripcion(documento.getDescripcion());
        dto.setIndHabilitado(documento.getIndHabilitado());
        return dto;
    }

    
}
