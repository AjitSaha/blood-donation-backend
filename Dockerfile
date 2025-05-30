# Use a Java runtime
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy your project files
COPY . .

# Build the app
RUN ./mvnw clean package -DskipTests

# Expose port (Spring Boot default)
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "target/*.jar"]
