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
	@Id
	@Column(name = "usu_id", length = 10)
	private String usuId ;

	@Column(name = "usu_id_colaborador", unique = true, nullable = false, length = 30)
	private String usuIdColaborador;
	
	@Column(name = "usu_login", unique=true, nullable=false, length = 10)
	private String usuLogin;
	
	@Column(name = "usu_contrasena", nullable = false, length = 80)
	private String usuContrasena;
	
	@Column(name = "ind_habilitado", nullable = false)
	private Boolean indHabilitado;
	
	 @CreationTimestamp
	 @Column(name = "fech_creacion", nullable = false, updatable = false)
	private LocalDateTime fechCreacion;
	
	 @UpdateTimestamp
	 @Column(name = "fech_actualizacion")
	private LocalDateTime fechActualizacion;
}
