# Multi-stage build for SemarangCar Spring Boot Application
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy configuration and source files
COPY pom.xml .
COPY src ./src

# Build production JAR package without running tests
RUN mvn clean package -DskipTests

# Minimal Alpine JRE runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy compiled JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose HTTP port 8080
EXPOSE 8080

# Run Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
