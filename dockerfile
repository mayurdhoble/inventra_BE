# Start from a base Java image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container
COPY target/business.inventra-0.0.1-SNAPSHOT.jar app.jar
# Expose the port your Spring Boot app runs on
EXPOSE 8889

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
