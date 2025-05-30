package com.habitar.colaboradores.servicio;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habitar.colaboradores.dto.ColaboradorActualizacionDTO;
import com.habitar.colaboradores.dto.ColaboradorCreacionDTO;
import com.habitar.colaboradores.dto.ColaboradorDTO;
import com.habitar.colaboradores.excepciones.ColaboradorNoEncontradoException;
import com.habitar.colaboradores.modelo.Colaborador;
import com.habitar.colaboradores.repositorio.ColaboradorRepositorio;


@Service
public class ColaboradorServicioImpl implements ColaboradorServicio {

	private final ColaboradorRepositorio colaboradorRepositorio;

	@Autowired
	public ColaboradorServicioImpl(ColaboradorRepositorio colaboradorRepositorio) {
		this.colaboradorRepositorio = colaboradorRepositorio;
	}

	@Override
	public ColaboradorDTO crearColaborador(ColaboradorCreacionDTO creacionDTO) {
		
		Colaborador colaborador = new Colaborador();
		colaborador.setNombre(creacionDTO.getNombre());
		colaborador.setApellido1(creacionDTO.getApellido1());
		colaborador.setDni(creacionDTO.getDni());
		colaborador.setEmail(creacionDTO.getEmail());
		colaborador.setProfesion(creacionDTO.getProfesion());
		colaborador.setFechaContratacion(creacionDTO.getFechaContratacion());
		colaborador.setSueldo(creacionDTO.getSueldo());

		Colaborador colaboradorGuardado = colaboradorRepositorio.save(colaborador);
		
		return convertirAColaboradorDTO(colaboradorGuardado);
		
		
	}

	@Override
	public List<ColaboradorDTO> obtenerTodosLosColaboradores() {

		List<Colaborador> colaboradores = colaboradorRepositorio.findAll();
		List<ColaboradorDTO> colaboradoresDto = colaboradores.stream()
															 .sorted((o1, o2) -> o1.getNombre().compareTo(o2.getNombre()))
															 .map(this::convertirAColaboradorDTO)
															 .collect(Collectors.toList());

		return colaboradoresDto;
	}

	@Override
	public Optional<ColaboradorDTO> obtenerColaboradorPorId(String id) {
		Optional<ColaboradorDTO> colaborador = colaboradorRepositorio.findById(id).map(this::convertirAColaboradorDTO); 
		//no tiramos la excelcpicion aqui
		//la decisin de qué hacer si el Optional está vacío se delega al controlador (ojito, patrón CHAIN OF RESPONSABILTY)
		
		return colaborador;
	}

	@Override
	public ColaboradorDTO actualizarColaborador(String id, ColaboradorActualizacionDTO actualizacionDTO) {

		Optional<Colaborador> colaboradorOptional = colaboradorRepositorio.findById(id);

		Colaborador colaborador = colaboradorOptional.orElseThrow(() -> new ColaboradorNoEncontradoException(id));

		Optional.ofNullable(actualizacionDTO.getNombre()).ifPresent(colaborador::setNombre);
		Optional.ofNullable(actualizacionDTO.getApellido1()).ifPresent(colaborador::setApellido1);
		Optional.ofNullable(actualizacionDTO.getDni()).ifPresent(colaborador::setDni);
		Optional.ofNullable(actualizacionDTO.getEmail()).ifPresent(colaborador::setEmail);
		Optional.ofNullable(actualizacionDTO.getProfesion()).ifPresent(colaborador::setProfesion);
		Optional.ofNullable(actualizacionDTO.getFechaContratacion()).ifPresent(colaborador::setFechaContratacion);
		Optional.ofNullable(actualizacionDTO.getSueldo()).ifPresent(colaborador::setSueldo);

		Colaborador colaboradorActualizado = colaboradorRepositorio.save(colaborador);
		return convertirAColaboradorDTO(colaboradorActualizado);
	}

	@Override //podriamos poner un aspecto para notificar la eliminación xd
	public void eliminarColaborador(String id) {
		colaboradorRepositorio.deleteById(id);
	}

	private ColaboradorDTO convertirAColaboradorDTO(Colaborador colaborador) {
		return new ColaboradorDTO(colaborador.getId(), colaborador.getNombre(), colaborador.getApellido1(),
				colaborador.getDni(), colaborador.getEmail(), colaborador.getProfesion(),
				colaborador.getFechaContratacion(), colaborador.getSueldo());
	}

}
