package com.habitar.autenticacion.modelo;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity  ///eso indica que es una entidad JPA -> osea la mapea a una tabla de la bd
@Table(name = "usuarios_habitar") //aqui se define la tabla (JAKARTA - JPA)
@Data
public class Usuario {

	/* 
	    usu_id VARCHAR(10) primary key unique not null,
		usu_id_colaborador varchar(30) unique not null,
		usu_login varchar(10) unique not null,
		usu_contrasena varchar(10) not null,
		ind_habilitado boolean not null default true,
		fech_creacion timestamp WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
		fech_actualizacion TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAM
		
	 */
	
	@Id
	@Column(name = "usu_id", length = 10)
	private String usu_id ;

	@Column(name = "usu_id_colaborador", unique = true, nullable = false, length = 30)
	private String usu_id_colaborador;
	
	@Column(name = "usu_login", unique=true, nullable=false, length = 10)
	private String usu_login;
	
	@Column(name = "usu_contrasena", nullable = false, length = 10)
	private String usu_contrasena;
	
	@Column(name = "ind_habilitado", nullable = false)
	private Boolean ind_habilitado;
	
	 @CreationTimestamp
	 @Column(name = "fech_creacion", nullable = false, updatable = false)
	private LocalDateTime fech_creacion;
	
	 @UpdateTimestamp
	 @Column(name = "fech_actualizacion")
	private LocalDateTime fech_actualizacion;
	
	
	
	
	
}
