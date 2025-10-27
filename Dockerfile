# Stage 1: Build the Application
# --- FIX: Change base image to one that includes Maven ---
FROM maven:3.9.6-openjdk-17-slim AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the build file (pom.xml) and download dependencies
COPY pom.xml .
# Download dependencies to a cached layer (Maven is now available)
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the Spring Boot application, creating a runnable JAR file
RUN mvn package -DskipTests

# Stage 2: Create the Final, Lightweight Runtime Image
# Use a JRE-only base image for production
FROM openjdk:17-jre-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
