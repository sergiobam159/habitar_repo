package com.habitar.proyectos.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.habitar.proyectos.dto.ProyectoActualizacionDTO;
import com.habitar.proyectos.dto.ProyectoCreacionDTO;
import com.habitar.proyectos.dto.ProyectoDTO;

public interface ProyectoServicio {
	
	 ProyectoDTO crearProyecto(ProyectoCreacionDTO proyectoCreacionDTO);

	    Optional<ProyectoDTO> obtenerProyectoPorId(String id); //OPTIONAL para la gestión de nulls

	    List<ProyectoDTO> obtenerTodosLosProyectos();

	    ProyectoDTO actualizarProyecto(String id, ProyectoActualizacionDTO proyectoActualizacionDTO);

	    void eliminarProyecto(String id);
	    
	    ProyectoDTO asignarResponsable(String proyectoId, String colaboradorId);
	    
	    Boolean checkProyectoExiste(String proyectoId);

}
