package com.habitar.proyectos.servicio;

import java.util.List;
import java.util.Optional;

public interface ProyectoServicio {
	
	 ProyectoDTO crearProyecto(ProyectoCreacionDTO proyectoCreacionDTO);

	    Optional<ProyectoDTO> obtenerProyectoPorId(String id); //OPTIONAL para la gestión de nulls

	    List<ProyectoDTO> obtenerTodosLosProyectos();

	    ProyectoDTO actualizarProyecto(String id, ProyectoActualizacionDTO proyectoActualizacionDTO);

	    void eliminarProyecto(String id);

}
