package com.habitar.proyectos.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.habitar.proyectos.modelo.Proyecto;

@Repository //indica que esta clase es un repositorio - patrón repository - gestión de acceso a datos (unica interfaz!)
public interface ProyectoRepositorio extends MongoRepository<Proyecto, String> { // para CUD - esta interfaz es de Spring Data MongoDB - usando genericos!! 

}
