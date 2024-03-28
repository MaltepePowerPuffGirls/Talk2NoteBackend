
FROM openjdk:21-jdk AS runtime
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "app.jar"]
