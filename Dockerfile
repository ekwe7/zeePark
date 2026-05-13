# Build stage
FROM gradle:8-jdk21 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN gradle build -x test

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8181

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8181/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
