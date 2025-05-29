package com.habitar.documentos.repositorio;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.habitar.documentos.model.Documento;

//JpaRepository nos da los crud CRUD  
public interface DocumentoRepositorio extends JpaRepository<Documento, Long>{ 
	
	//en todas siempre se debe filtrar ind_habilitado true
	
	List<Documento> findByIdProyecto(String idProyecto);

    
    Optional<Documento> findByIdBlobStorage(String idBlobStorage);
	
}
