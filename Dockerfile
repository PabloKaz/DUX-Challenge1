# Etapa de compilación
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace

# Copiamos pom, el wrapper de Maven y el código fuente
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src

# Damos permiso de ejecución al mvnw antes de invocarlo
RUN chmod +x mvnw

# Cacheamos dependencias
RUN ./mvnw dependency:go-offline -B

# Compilamos el JAR (sin tests)
RUN ./mvnw clean package -DskipTests -B

# -----------------------------

# Etapa de runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=build /workspace/target/*.jar app.jar

# Ejecutamos la aplicación
ENTRYPOINT ["java","-jar","/app/app.jar"]
