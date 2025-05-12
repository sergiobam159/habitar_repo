package com.habitar.proyectos.modelo;

import java.time.LocalDateTime;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data  //esto es de lombok, quita la carga cognitiva de creación de getters y setter =d

@Document(collection = "proyectos") //este tag es el que indica que esto de de la coleccion de proyectos en mongobd
public class Proyecto {
 @Id
 private String id;
 @Field("nombre")
 private String nombre;
 @Field("descripcion")
 private String descripcion;
 @Field("estado")
 private String estado;
 @Field("cliente_id")
 private String clienteId;
 @Field("colaborador_responsable_id")
 private String colaboradorResponsableId; //id obtenido desde el servicio de controlador
 @Field("fecha_de_inicio")
 private LocalDateTime fechaDeInicio;
}
