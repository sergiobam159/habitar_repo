package com.habitar.proyectos.servicio;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.habitar.proyectos.dto.ProyectoActualizacionDTO;
import com.habitar.proyectos.dto.ProyectoCreacionDTO;
import com.habitar.proyectos.dto.ProyectoDTO;
import com.habitar.proyectos.excepciones.ProyectoNoEncontradoExcepcion;
import com.habitar.proyectos.modelo.Proyecto;
import com.habitar.proyectos.repositorio.ProyectoRepositorio;

@Service
public class ProyectoServicioImpl implements ProyectoServicio {

	private final ProyectoRepositorio proyectoRepositorio; // inmutable, privado

	@Autowired // ← inyeccion de dependencias - necesita la instancia de ProyectoRepositorio
				// para que ProyectoServicioImplfuncione
				//// principio de inversión de control (IoC) - inversión de dependencias
	public ProyectoServicioImpl(ProyectoRepositorio proyectoRepositorio) {
		this.proyectoRepositorio = proyectoRepositorio;
	}

	
	@Override
	public Boolean checkProyectoExiste (String Id) {
		
		return Optional.ofNullable(this.obtenerProyectoPorId(Id)).filter(p -> !p.isEmpty()).isPresent();
	}
	
	@Override
	public ProyectoDTO crearProyecto(ProyectoCreacionDTO proyectoCreacionDTO) {

		// aqui se instancia el nuevo proyecto y se asignan los get del DTO generado
		// desde controlador
		Proyecto proyecto = new Proyecto();
		proyecto.setNombre(proyectoCreacionDTO.getNombre());
		proyecto.setDescripcion(proyectoCreacionDTO.getDescripcion());
		proyecto.setEstado(proyectoCreacionDTO.getEstado());
		proyecto.setClienteId(proyectoCreacionDTO.getClienteId());
		proyecto.setColaboradorResponsableId(proyectoCreacionDTO.getColaboradorResponsableId());
		proyecto.setFechaDeInicio(proyectoCreacionDTO.getFechaDeInicio());

		// aqui se guarda usando el repositorio (patron repositorio xd)
		// este metodo .save retorna un objeto proyecto, el que se guardó y tiene el
		// nuevo id
		Proyecto proyectoGuardado = proyectoRepositorio.save(proyecto);

		// luego se genera un dto del proyecto guardado para retornar
		// con el nuevo id generado
		ProyectoDTO proyectoDTO = new ProyectoDTO();
		proyectoDTO.setId(proyectoGuardado.getId());
		proyectoDTO.setNombre(proyectoGuardado.getNombre());
		proyectoDTO.setDescripcion(proyectoGuardado.getDescripcion());
		proyectoDTO.setEstado(proyectoGuardado.getEstado());
		proyectoDTO.setClienteId(proyectoGuardado.getClienteId());
		proyectoDTO.setColaboradorResponsableId(proyectoGuardado.getColaboradorResponsableId());
		proyectoDTO.setFechaDeInicio(proyectoGuardado.getFechaDeInicio());

		return proyectoDTO;
	}

	@Override
	public Optional<ProyectoDTO> obtenerProyectoPorId(String id) { // USO DE OJBETO OPTIONAL

		Optional<Proyecto> proyectoOptional = proyectoRepositorio.findById(id); // GESTION DE NULL

		return proyectoOptional.map(proyecto -> { // metodo map que recibe una funcion
			// expresion lambda con 1 parametro

			ProyectoDTO proyectoDTO = new ProyectoDTO();

			proyectoDTO.setId(proyecto.getId());
			proyectoDTO.setNombre(proyecto.getNombre());
			proyectoDTO.setDescripcion(proyecto.getDescripcion());
			proyectoDTO.setEstado(proyecto.getEstado());
			proyectoDTO.setClienteId(proyecto.getClienteId());
			proyectoDTO.setColaboradorResponsableId(proyecto.getColaboradorResponsableId());
			proyectoDTO.setFechaDeInicio(proyecto.getFechaDeInicio());

			return proyectoDTO;

		});

	}

	@Override
	public List<ProyectoDTO> obtenerTodosLosProyectos() {

		List<Proyecto> proyectos = proyectoRepositorio.findAll();

		return proyectos.stream().map(proyecto -> { // metodo intermedio
			ProyectoDTO proyectoDTO = new ProyectoDTO();

			proyectoDTO.setId(proyecto.getId());
			proyectoDTO.setNombre(proyecto.getNombre());
			proyectoDTO.setDescripcion(proyecto.getDescripcion());
			proyectoDTO.setEstado(proyecto.getEstado());
			proyectoDTO.setClienteId(proyecto.getClienteId());
			proyectoDTO.setColaboradorResponsableId(proyecto.getColaboradorResponsableId());
			proyectoDTO.setFechaDeInicio(proyecto.getFechaDeInicio());

			return proyectoDTO; // Este map recorre proyectos obtenidos por el findAll, y genera DTOs por cada
								// proyecto

		}).collect(Collectors.toList()); // luego este metodo final, collect, los convierte en lista :)

	}

	@Override
	public ProyectoDTO actualizarProyecto(String id, ProyectoActualizacionDTO proyectoActualizacionDTO) {

		// aca busca por el id del proyecto a actualizar
		Optional<Proyecto> proyectoOptional = proyectoRepositorio.findById(id);

		// aqui se gestiona la exepcion perosnalizada
		// en caso haya null
		// es una funcion lambda sin variables, productora
		Proyecto proyecto = proyectoOptional.orElseThrow(() -> new ProyectoNoEncontradoExcepcion(id));

		// aca se actualizan solo los campos si los valores en el DTO no son nulos
		// proyecto.setId(id);
		if (proyectoActualizacionDTO.getNombre() != null) {
			proyecto.setNombre(proyectoActualizacionDTO.getNombre());
		}
		if (proyectoActualizacionDTO.getDescripcion() != null) {
			proyecto.setDescripcion(proyectoActualizacionDTO.getDescripcion());
		}
		if (proyectoActualizacionDTO.getEstado() != null) {
			proyecto.setEstado(proyectoActualizacionDTO.getEstado());
		}
		if (proyectoActualizacionDTO.getClienteId() != null) {
			proyecto.setClienteId(proyectoActualizacionDTO.getClienteId());
		}
		if (proyectoActualizacionDTO.getColaboradorResponsableId() != null) {
			proyecto.setColaboradorResponsableId(proyectoActualizacionDTO.getColaboradorResponsableId());
		}
		if (proyectoActualizacionDTO.getFechaDeInicio() != null) {
			proyecto.setFechaDeInicio(proyectoActualizacionDTO.getFechaDeInicio());
		}
		Proyecto proyectoActualizado = proyectoRepositorio.save(proyecto);

		ProyectoDTO proyectoDTO = new ProyectoDTO();

		proyectoDTO.setId(proyectoActualizado.getId());
		proyectoDTO.setNombre(proyectoActualizado.getNombre());
		proyectoDTO.setDescripcion(proyectoActualizado.getDescripcion());
		proyectoDTO.setEstado(proyectoActualizado.getEstado());
		proyectoDTO.setClienteId(proyectoActualizado.getClienteId());
		proyectoDTO.setColaboradorResponsableId(proyectoActualizado.getColaboradorResponsableId());
		proyectoDTO.setFechaDeInicio(proyectoActualizado.getFechaDeInicio());

		return proyectoDTO;
	}

	@Override
	public void eliminarProyecto(String id) {

		if (!proyectoRepositorio.existsById(id)) {
			throw new ProyectoNoEncontradoExcepcion(id);
		} else {
			proyectoRepositorio.deleteById(id);
		}

	}

	@Override
	public ProyectoDTO asignarResponsable(String proyectoId, String colaboradorId) {
		Proyecto proyecto = proyectoRepositorio.findById(proyectoId)
				.orElseThrow(() -> new ProyectoNoEncontradoExcepcion(proyectoId));
		proyecto.setColaboradorResponsableId(colaboradorId);
			
		proyectoRepositorio.save(proyecto);
		
		ProyectoDTO proyectoDTO = new ProyectoDTO();

		proyectoDTO.setId(proyecto.getId());
		proyectoDTO.setNombre(proyecto.getNombre());
		proyectoDTO.setDescripcion(proyecto.getDescripcion());
		proyectoDTO.setEstado(proyecto.getEstado());
		proyectoDTO.setClienteId(proyecto.getClienteId());
		proyectoDTO.setColaboradorResponsableId(proyecto.getColaboradorResponsableId());
		proyectoDTO.setFechaDeInicio(proyecto.getFechaDeInicio());
		return proyectoDTO;

	}

}
