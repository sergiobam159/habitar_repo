package com.habitar.proyectos.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habitar.proyectos.dto.ProyectoActualizacionDTO;
import com.habitar.proyectos.dto.ProyectoCreacionDTO;
import com.habitar.proyectos.dto.ProyectoDTO;
import com.habitar.proyectos.modelo.Proyecto;
import com.habitar.proyectos.repositorio.ProyectoRepositorio;


@Service
public class ProyectoServicioImpl implements ProyectoServicio  {
	
	private final ProyectoRepositorio proyectoRepositorio; //inmutable, privado 
	 
	@Autowired  //← inyeccion de dependencias - necesita la instancia de ProyectoRepositorio
				//para que ProyectoServicioImplfuncione
	     		////principio de inversión de control (IoC) - inversión de dependencias
	public ProyectoServicioImpl(ProyectoRepositorio proyectoRepositorio) {
	        this.proyectoRepositorio = proyectoRepositorio;
	    }
	
	@Override
	public ProyectoDTO crearProyecto(ProyectoCreacionDTO proyectoCreacionDTO) {
		 
		//aqui se instancia el nuevo proyecto y se asignan los get del DTO generado desde controlador 
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(proyectoCreacionDTO.getNombre());
        proyecto.setDescripcion(proyectoCreacionDTO.getDescripcion());
        proyecto.setEstado(proyectoCreacionDTO.getEstado());
        proyecto.setClienteId(proyectoCreacionDTO.getClienteId());
        proyecto.setColaboradorResponsableId(proyectoCreacionDTO.getColaboradorResponsableId());
        proyecto.setFechaDeInicio(proyectoCreacionDTO.getFechaDeInicio());

        //aqui se guarda usando el repositorio (patron repositorio xd)
        //este metodo .save retorna un objeto proyecto, el que se guardó y tiene el nuevo id
        Proyecto proyectoGuardado = proyectoRepositorio.save(proyecto);

        //luego se genera un dto del proyecto guardado para retornar
        //con el nuevo id generado
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
	public Optional<ProyectoDTO> obtenerProyectoPorId(String id) { //USO DE OJBETO OPTIONAL 
		Optional<Proyecto> proyectoOptional = proyectoRepositorio.findById(id); //GESTION DE NULL
		
		return proyectoOptional.map(proyecto -> { //metodo map que recibe una funcion 
			//expresion lambda con 1 parametro
            
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProyectoDTO actualizarProyecto(String id, ProyectoActualizacionDTO proyectoActualizacionDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminarProyecto(String id) {
		// TODO Auto-generated method stub
		
	}

}
