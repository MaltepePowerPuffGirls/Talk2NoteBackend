
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM openjdk:21-jdk

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ./target/Talk2NoteBackend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/app.jar"]