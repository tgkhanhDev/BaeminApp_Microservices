# Use an official OpenJDK runtime as a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /root/java-gateway

# Copy the generated JAR file from the Maven build step to the container
COPY target/*.jar java-gateway.jar

# Expose the application's port (default is 8080)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "java-gateway.jar"]