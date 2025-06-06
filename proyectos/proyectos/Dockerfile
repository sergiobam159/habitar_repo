# syntax=docker/dockerfile:1

# --- Build stage ---
FROM openjdk:17-jdk-slim AS build
WORKDIR /app

# Copy Maven wrapper and pom.xml first for dependency caching
COPY --link pom.xml mvnw ./
COPY --link .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copy the rest of the source code
COPY --link src ./src

# Build the application (skip tests for faster build)
RUN ./mvnw package -DskipTests

# --- Runtime stage ---
FROM eclipse-temurin:17-jre
WORKDIR /app

# Create a non-root user and group
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Set permissions
RUN chown appuser:appgroup /app/app.jar
USER appuser

# JVM options: container-aware memory settings
ENV JAVA_OPTS="-XX:MaxRAMPercentage=80.0 -XX:+UseContainerSupport"

# Expose the default Spring Boot port (optional, for documentation)
EXPOSE 8080

# Use exec form for proper signal handling
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
