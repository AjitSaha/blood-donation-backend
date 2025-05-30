# Use an official JDK 21 image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy the project files
COPY . .

# Make mvnw executable (needed for Render)
RUN chmod +x mvnw

# Build the Spring Boot project
RUN ./mvnw clean package -DskipTests

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Start the app
CMD ["java", "-jar", "target/*.jar"]
