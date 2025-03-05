# Use OpenJDK 17 as base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the built application JAR into the container
COPY target/*.jar app.jar

# Expose application port (adjust based on your Spring Boot app)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
