
FROM amazoncorretto:17

ARG JAR_FILE=target/*.jar

COPY ./target/Talk2NoteBackend-0.0.1-SNAPSHOT.jar app.jar

CMD apt-get update -y

ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/app.jar"]