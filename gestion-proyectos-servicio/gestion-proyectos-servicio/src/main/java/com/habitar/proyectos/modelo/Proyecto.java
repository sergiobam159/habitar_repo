package com.habitar.proyectos.modelo;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data  //esto es de lombok, quita la carga cognitiva de creación de getters y setter =d
@Document(collection = "proyectos") //este tag es el que indica que esto de de la coleccion de proyectos en mongobd
public class Proyecto {
 @Id
 private String id;
 private String nombre;
 private String descripcion;
 private String estado;
 private String clienteId;
 private String colaboradorResponsableId; //id obtenido desde el servicio de controlador
 private Date fechaDeInicio;
 
 
 

}
