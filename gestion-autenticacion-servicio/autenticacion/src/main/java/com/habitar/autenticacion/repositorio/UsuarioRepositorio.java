package com.habitar.autenticacion.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.habitar.autenticacion.modelo.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{ //ESTE EXTIENDE DE JPA por que es una BD RELACIONAL POSTGRES

	Optional<Usuario> findByUsuLogin(String usu_login);

    Optional<Usuario> findByUsuIdColaborador(String usuIdColaborador);
	
}
