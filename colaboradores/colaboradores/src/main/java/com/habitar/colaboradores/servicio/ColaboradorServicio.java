package com.habitar.colaboradores.servicio;

import java.util.List;
import java.util.Optional;

import com.habitar.colaboradores.dto.ColaboradorActualizacionDTO;
import com.habitar.colaboradores.dto.ColaboradorCreacionDTO;
import com.habitar.colaboradores.dto.ColaboradorDTO;

public interface ColaboradorServicio {
	
	ColaboradorDTO crearColaborador(ColaboradorCreacionDTO creacionDTO);
    List<ColaboradorDTO> obtenerTodosLosColaboradores();
    Optional<ColaboradorDTO> obtenerColaboradorPorId(String id);
    ColaboradorDTO actualizarColaborador(String id, ColaboradorActualizacionDTO actualizacionDTO);
    void eliminarColaborador(String id);

    
	
	
}
