# 1) Stage de compilación: construye el JAR con Maven
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src
# Prepara dependencias (cachea para no volver a bajarlas cada build)
RUN ./mvnw dependency:go-offline -B
# Ahora compila el proyecto
RUN ./mvnw clean package -DskipTests -B

# 2) Stage final: solo el runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia únicamente el JAR generado en el stage anterior
COPY --from=build /workspace/target/DUX-Challenge-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que corre Spring Boot
EXPOSE 8080

# Comando por defecto al levantar el contenedor
ENTRYPOINT ["java","-jar","/app/app.jar"]
