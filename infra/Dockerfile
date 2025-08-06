# Use a lightweight Java 24 runtime
FROM eclipse-temurin:24-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the Spring Boot fat jar
COPY target/platform-1.0-SNAPSHOT.jar.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]