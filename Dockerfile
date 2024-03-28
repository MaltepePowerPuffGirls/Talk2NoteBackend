
FROM openjdk:21-jdk

RUN ["mvn", "build"]

ARG JAR_FILE=target/*.jar

COPY ./target/Talk2NoteBackend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/app.jar"]