# First stage: Build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .

# Download the dependencies
RUN mvn dependency:go-offline

# Copy the rest of the project files
COPY src ./src

# Build the project
RUN mvn clean install

# Second stage: Create the runtime image
FROM amazoncorretto:17

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/Talk2NoteBackend-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the application port (replace with your application's port)
EXPOSE 8080

# Add a health check to ensure the container is running correctly
HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl --fail http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/app/app.jar"]
