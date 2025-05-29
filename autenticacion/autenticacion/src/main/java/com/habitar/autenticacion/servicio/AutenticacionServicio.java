package com.habitar.autenticacion.servicio;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.habitar.autenticacion.modelo.Usuario;
import com.habitar.autenticacion.repositorio.UsuarioRepositorio;

@Service
public class AutenticacionServicio implements UserDetailsService {

	 private final UsuarioRepositorio usuarioRepositorio;

	    public AutenticacionServicio(UsuarioRepositorio usuarioRepositorio) {
	        this.usuarioRepositorio = usuarioRepositorio;
	    }

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOp = usuarioRepositorio.findByUsuLogin(username);
		Usuario usuario = usuarioOp.orElseThrow(()-> new UsernameNotFoundException("No se ha encontrado el usuario con el login "+username));
		
		return User.builder()
				.username(usuario.getUsuLogin())
				.password(usuario.getUsuContrasena())
				.build();
		
	}

}
