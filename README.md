# habitar

╔════════════════════════════════════════════════════════════════════╗  
║                  HABITAR - estudio de arquitectura                ║  
╚════════════════════════════════════════════════════════════════════╝  

---
 
**Inspirado en la tierra,  
la arcilla y la vida**

  
Sistema de gestión de proyectos Estudio de Arquitectura Habitar 

Consta de modulos para la gestión de proyectos, profesionales responsables, y documentación del proyecto
Se necesita tener un usuario validado para poder realizar peticiones

Se recomienda utilizar Postman para probar las peticiones

El archivo de peticiones postman está en el repositorio asi que no ande de lloron 

## Ejecución con Docker

Este proyecto utiliza múltiples microservicios Java (Spring Boot) y está preparado para ejecutarse fácilmente con Docker y Docker Compose. Cada microservicio tiene su propio Dockerfile y se recomienda usar Docker Compose para levantar todo el sistema de forma coordinada.

### Requisitos
- Docker y Docker Compose instalados
- Java 17 (usado en las imágenes base: `eclipse-temurin:17-jdk` y `eclipse-temurin:17-jre`)

### Puertos expuestos por servicio
- **api-gateway:** 8080
- **config-server:** 8888
- **autenticacion:** 8081 (mapeado a 8080 interno)
- **clientes:** 8082 (mapeado a 8080 interno)
- **colaboradores:** 8083 (mapeado a 8080 interno)
- **documentos:** 8084 (mapeado a 8080 interno)
- **proyectos:** 8085 (mapeado a 8080 interno)

### Variables de entorno
- Puedes definir variables de entorno específicas para cada microservicio usando archivos `.env` en los respectivos directorios de cada servicio. Si existen, descomenta la línea `env_file` correspondiente en el `docker-compose.yml`.

### Instrucciones para construir y ejecutar
1. Asegúrate de tener Docker y Docker Compose instalados.
2. Desde la raíz del proyecto, ejecuta:

   ```sh
   docker compose up --build
   ```

   Esto construirá y levantará todos los servicios definidos en el archivo `docker-compose.yml`.

3. Accede a los servicios a través de los puertos indicados arriba. Por ejemplo, el API Gateway estará disponible en `http://localhost:8080`.

### Notas adicionales
- Todos los servicios usan imágenes basadas en Java 17 y están configurados para ejecutarse como usuarios no root dentro del contenedor.
- El `config-server` debe estar disponible antes de que los demás servicios dependientes arranquen correctamente (esto se gestiona automáticamente con `depends_on` en Docker Compose).
- Si necesitas personalizar opciones de JVM, puedes hacerlo mediante la variable de entorno `JAVA_OPTS` en cada servicio.

