#multi-stage build
#separa la fase de compialción y ejecución

#primera fase: compilación -> compila la aplicación con Maven, generando el JAR
FROM maven:3.9.4-eclipse-temurin-17 as build
#define la imagen origen - java 17 - esta imagen base tiene el maven para la generación del JAR, luego usamos otra
#aqui va a compilar

WORKDIR /app
#define la carpeta raiz del ms como directorio de chamba
#aqui correra los comandos

COPY pom.xml .
#copia el pom del local al workdir
COPY src ./src
#mueve todo lo del src al workdir (el codigo fuente pes)
RUN mvn clean package -DskipTests
#aqui ejecuta maven clean y genera el jar

#segunda fase: ejecución -> crea una imagen conteniendo solo el JAR y el JRE 
FROM openjdk:17-jdk-slim
#inicia la segunda fase, luego de haber construido el jar

WORKDIR /app
#lo mismo, define el workdir para la segunda fase
COPY --from=build /app/target/*.jar app.jar
#copia el jar en la imagen entiempo de ejeución

ENTRYPOINT ["java", "-jar", "app.jar"]
#aqui ejecuta el jar
#define los comandos para ejecución