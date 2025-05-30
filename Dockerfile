# Use a base image with Java 21 support
FROM eclipse-temurin:21-jdk AS build

# Set working directory
WORKDIR /app

# Copy everything
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Second stage to run the app
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/BloodDonationAjit-0.0.1-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
