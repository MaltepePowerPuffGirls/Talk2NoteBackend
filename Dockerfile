
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install

FROM amazoncorretto:17

ARG JAR_FILE=target/*.jar

COPY ./target/Talk2NoteBackend-0.0.1-SNAPSHOT.jar app.jar

CMD apt-get update -y

ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/app.jar"]