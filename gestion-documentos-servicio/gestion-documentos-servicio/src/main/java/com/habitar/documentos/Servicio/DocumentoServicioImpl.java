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

import com.habitar.documentos.cliente.ProyectoServiceCliente;
import com.habitar.documentos.dto.DocumentoDTO;
import com.habitar.documentos.model.Documento;
import com.habitar.documentos.repositorio.DocumentoRepositorio;

import jakarta.transaction.Transactional;

@Service
public class DocumentoServicioImpl implements DocumentoServicio {


    private final DocumentoRepositorio documentoRepositorio;
    private final AzureBlobStorageServicio azureBlobStorageServicio;
    private final ProyectoServiceCliente proyectoServiceCliente;

    
    public DocumentoServicioImpl(DocumentoRepositorio documentoRepositorio,
    							AzureBlobStorageServicio azureBlobStorageServicio,
                                ProyectoServiceCliente proyectoServiceCliente) {
        this.documentoRepositorio = documentoRepositorio;
        this.azureBlobStorageServicio = azureBlobStorageServicio;
        this.proyectoServiceCliente = proyectoServiceCliente;
    }

    @Override
    @Transactional
    public DocumentoDTO subirDocumento(MultipartFile file, String idProyecto) {
    	 Optional.ofNullable(idProyecto) // genera un optional, vacio o lleno dependiendo si existe o no  (ofnullable)
         .filter(id -> !id.isEmpty()) //filtro si no esta vacío empty y si ESTA VACIO thoweo la excepcion
         .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del proyecto no puede ser nulo o vacío."));


        try {
        	 if(!proyectoServiceCliente.checkProyectoExiste(idProyecto).getBody()) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El proyecto con ID " + idProyecto + " no existe.");
        	 }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo verificar la existencia del proyecto. Servicio de Proyectos no disponible o error de comunicación.", e);
        }

        try {
            String idBlobStorage = azureBlobStorageServicio.subirArchivo(file);
            String urlAlmacenamiento = azureBlobStorageServicio.obtenerUrl(idBlobStorage);

            Documento documento = new Documento();
            documento.setNombreArchivoOriginal(file.getOriginalFilename());
            documento.setTipoContenido(file.getContentType());
            documento.setTamanoArchivo(file.getSize());
            documento.setIdBlobStorage(idBlobStorage);
            documento.setUrlAlmacenamiento(urlAlmacenamiento);
            documento.setIdProyecto(idProyecto);
            documento.setFechaSubida(LocalDateTime.now());
            documento.setIndHabilitado(true); // Nuevo campo, por defecto habilitado

            Documento savedDocumento = documentoRepositorio.save(documento);
            return mapToDTO(savedDocumento);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar el archivo para subir.", e);
        }
    }

    @Override
    public byte[] descargarDocumento(String idBlobStorage) {
        Documento documento = documentoRepositorio.findByIdBlobStorage(idBlobStorage)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Metadatos de documento no encontrados para ID de blob: " + idBlobStorage));

        if (!documento.getIndHabilitado()) {
            throw new ResponseStatusException(HttpStatus.GONE, "El documento con ID " + idBlobStorage + " no está habilitado para descarga.");
        }

        try {
            return azureBlobStorageServicio.descargarArchivo(idBlobStorage);
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
                .filter(Documento::getIndHabilitado)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DocumentoDTO> obtenerDocumentoPorId(Long id) {
        return documentoRepositorio.findById(id)
                .filter(Documento::getIndHabilitado) // Solo devolvemos si está habilitado
                .map(this::mapToDTO);
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

    private DocumentoDTO mapToDTO(Documento documento) {
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

    // Método para obtener el tipo de contenido del archivo desde el nombre o la extensión
    // Puedes colocar esto en una clase utilitaria o dejarlo aquí si solo lo usa este servicio.
    // Aunque MultipartFile ya proporciona file.getContentType(), esto es útil como fallback.
    private String getContentType(String filename) {
        if (filename == null || filename.isEmpty()) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default binary type
        }
        String extension = "";
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i + 1);
        }
        switch (extension.toLowerCase()) {
            case "pdf": return MediaType.APPLICATION_PDF_VALUE;
            case "jpg":
            case "jpeg": return MediaType.IMAGE_JPEG_VALUE;
            case "png": return MediaType.IMAGE_PNG_VALUE;
            case "gif": return MediaType.IMAGE_GIF_VALUE;
            case "txt": return MediaType.TEXT_PLAIN_VALUE;
            case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xlsx": return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "pptx": return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            default: return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }

}
