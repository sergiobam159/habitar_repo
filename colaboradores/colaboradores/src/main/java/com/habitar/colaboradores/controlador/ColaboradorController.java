package com.habitar.colaboradores.controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habitar.colaboradores.dto.ColaboradorActualizacionDTO;
import com.habitar.colaboradores.dto.ColaboradorCreacionDTO;
import com.habitar.colaboradores.dto.ColaboradorDTO;
import com.habitar.colaboradores.excepciones.ColaboradorNoEncontradoException;
import com.habitar.colaboradores.servicio.ColaboradorServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {
	  private final ColaboradorServicio colaboradorServicio;
	  
	  @Autowired //no es necesario pero me siento mas comodo poniendolo
	  public ColaboradorController (ColaboradorServicio colaboradorServicio) {
		  this.colaboradorServicio=colaboradorServicio;
	  }
	  
	  @PostMapping
	    public ResponseEntity<ColaboradorDTO> crearColaborador(@Valid @RequestBody ColaboradorCreacionDTO colaboradorCreacionDTO) {
	        ColaboradorDTO colaboradorGuardado = colaboradorServicio.crearColaborador(colaboradorCreacionDTO);
	        return new ResponseEntity<>(colaboradorGuardado, HttpStatus.CREATED);
	    }

	    @GetMapping	
	    public ResponseEntity<List<ColaboradorDTO>> listarColaboradores() {
	        List<ColaboradorDTO> colaboradores = colaboradorServicio.obtenerTodosLosColaboradores();
	        return new ResponseEntity<>(colaboradores, HttpStatus.OK);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<ColaboradorDTO> obtenerColaboradorPorId(@PathVariable String id) {
	        Optional<ColaboradorDTO> colaborador = colaboradorServicio.obtenerColaboradorPorId(id);
	        System.out.println("GET EXITOSO A COLABORADORES");
	        return colaborador.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
	                          .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<ColaboradorDTO> actualizarColaborador(@PathVariable String id, @Valid @RequestBody ColaboradorActualizacionDTO colaboradorActualizacionDTO) {
	        try {
	            ColaboradorDTO colaboradorActualizado = colaboradorServicio.actualizarColaborador(id, colaboradorActualizacionDTO);
	            return new ResponseEntity<>(colaboradorActualizado, HttpStatus.OK);
	        } catch (ColaboradorNoEncontradoException e) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> eliminarColaborador(@PathVariable String id) {
	        try {
	            colaboradorServicio.eliminarColaborador(id);
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        } catch (ColaboradorNoEncontradoException e) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }

	    // el handler de la excepción de los validadores 
	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<java.util.Map<String, String>> handleValidationExceptions(org.springframework.web.bind.MethodArgumentNotValidException ex) {
	        java.util.Map<String, String> errors = new java.util.HashMap<>();
	        ex.getBindingResult().getFieldErrors().forEach(error -> {
	            errors.put(error.getField(), error.getDefaultMessage());
	        });
	        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	    }

	    // Manejo de la excepción generada al no encontrar colaborador 
	    @ExceptionHandler(ColaboradorNoEncontradoException.class)
	    public ResponseEntity<Void> handleColaboradorNoEncontrado(ColaboradorNoEncontradoException ex) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
}
