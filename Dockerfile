# Stage 1: Build Stage
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . /app
RUN mvn -B clean package

# Stage 2: Runtime Stage
FROM openjdk:21-jdk AS runtime
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "app.jar"]
