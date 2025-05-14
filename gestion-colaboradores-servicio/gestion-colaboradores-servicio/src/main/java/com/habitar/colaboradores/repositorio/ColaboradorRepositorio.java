package com.habitar.colaboradores.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;


import com.habitar.colaboradores.modelo.Colaborador;

public interface ColaboradorRepositorio extends MongoRepository<Colaborador, String>{

}
