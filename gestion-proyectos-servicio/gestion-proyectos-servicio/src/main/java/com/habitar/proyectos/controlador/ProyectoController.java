package com.habitar.proyectos.controlador;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habitar.proyectos.dto.ProyectoActualizacionDTO;
import com.habitar.proyectos.dto.ProyectoCreacionDTO;
import com.habitar.proyectos.dto.ProyectoDTO;
import com.habitar.proyectos.excepciones.ProyectoNoEncontradoExcepcion;
import com.habitar.proyectos.servicio.ProyectoServicio;

import jakarta.validation.Valid;

@RestController  //etiqueta de controlador REST - este maneja los metodos http =p
@RequestMapping("/proyectos") //luego esto de RequestMapping, este es el que define la ruta (http://localhost/proyectos/etc)
public class ProyectoController {

	private final ProyectoServicio proyectoServicio;  //instancia singleton del servicio =)
	
	
	@Autowired //inyección de dependencias,  necesita una instancia de ProyectoServicio para instanciar ProyectoController
	//OJO AQUI --gemini me dice que autowired es opcional, pq spring framework que estamos usando es mayor a 4.5 (spring boot 3.4)
	public ProyectoController(ProyectoServicio proyectoServicio) {	
		this.proyectoServicio = proyectoServicio;		
	}
	@PostMapping // esto indica que este metodo gestiona los HTTP POST de /proyectos 
	//ResponseEntity hereda de la clas HttpEntity - representa toda la respuesta HTTP incluyendo el cuerpo (proyectoDTO) y estado
    public ResponseEntity<ProyectoDTO> crearProyecto(@Valid @RequestBody ProyectoCreacionDTO proyectoCreacionDTO) { 
		//etiqueta @RequestBody indica que el proyectoCreacionDTO se llenara con el body de la petición
        ProyectoDTO proyectoGuardado = proyectoServicio.crearProyecto(proyectoCreacionDTO); 
        //por qué esta usando proyectoServicio y no proyectoServicioImp? por la inyección de dependencias
        //el IOC de spring se encarga de proporcionar la implementación 
        
        /*ya que se ha anotado la clase ProyectoServicioImpl con @Service
         *  esto la convierte en un bean gestionado por Spring y lo identifica como
         *  una implementación válida de la interfaz ProyectoServicio
         *  por lo cual el @autowired puede encontrar la implementación de ProyectoServicio
         */
        return new ResponseEntity<>(proyectoGuardado, HttpStatus.CREATED);
    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarProyecto(@PathVariable String id) { //los id por lo general se pasan como pathvariable
		 proyectoServicio.eliminarProyecto(id);		
		 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping
	public ResponseEntity<List<ProyectoDTO>> listarProyectos(){
		List<ProyectoDTO> todosLosProyectos= proyectoServicio.obtenerTodosLosProyectos();
		
		return new ResponseEntity<>(todosLosProyectos, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<ProyectoDTO>> obtenerProyectoPorId (@PathVariable String id){
		Optional<ProyectoDTO> proyectoEncontrado = proyectoServicio.obtenerProyectoPorId(id);
		return new ResponseEntity<>(proyectoEncontrado, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProyectoDTO> ActualizarProyecto ( @PathVariable String id,@Valid @RequestBody ProyectoActualizacionDTO proyectoActualizacionDTO) {
		
		ProyectoDTO proyectoActualizado= proyectoServicio.actualizarProyecto(id, proyectoActualizacionDTO);
		
		return new ResponseEntity<>(proyectoActualizado, HttpStatus.OK);
	}
	  @ExceptionHandler(ProyectoNoEncontradoExcepcion.class)  
	  //este @ indica que este metodo se encargara de las excepciones ProyectoNoEncontradoExcepcion
	  //tageadas en la excepcion HttpStatus.NOT_FOUND -- basicamente le pone el cuerpo que definimos al crear la excepción personalizada
	    public ResponseEntity<String> handleProyectoNoEncontrado(ProyectoNoEncontradoExcepcion ex) {
	        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	    }
	  
	  
	  @ExceptionHandler(MethodArgumentNotValidException.class) //aqui gestiona las excepciones del validator, que revisa los argumentos
	    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
	        Map<String, String> errors = new HashMap<String, String>();
	        
	        ex.getBindingResult().getAllErrors().forEach((error) -> {  //itera sobre todos los errores de validación
	            String fieldName = ((FieldError) error).getField(); //casteo a error de campo tipo FieldError - el nombre del campo que falló
	            String errorMessage = error.getDefaultMessage(); //los mensajes dentro de las anotaciones 
	            errors.put(fieldName, errorMessage);  //los mete al map con el nombre del campo como clave y el mensaje de error como valor (lindo)
	        });
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); //con el mapa arma un response entity
	    }
}
