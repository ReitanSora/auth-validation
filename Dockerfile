# 1. Build stage: cacheo de dependencias de Gradle
FROM gradle:8.7-jdk17 AS build

WORKDIR /home/gradle/src
# Copiamos sólo los archivos de build para descargar dependencias
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

# Ahora copiamos el resto del código y compilamos
COPY --chown=gradle:gradle . .
RUN gradle assemble --no-daemon

# 2. Runtime stage: imagen de JRE Alpine (muy ligera) y sin root
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
# Creamos un usuario no root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Exponemos el puerto de la app
EXPOSE 8080

# Copiamos el JAR desde el build stage
COPY --from=build /home/gradle/src/build/libs/auth-validation-0.0.1-SNAPSHOT.jar ./auth-validation.jar

# Healthcheck para Docker/Orquestadores
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Ejecutamos con las JVM options parametrizadas
ENTRYPOINT ["java", "-Xms256m","-Xmx512m", "-jar", "/app/auth-validation.jar"]
