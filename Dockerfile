# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy gradle config and wrapper
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src src

# Make wrapper executable and build the bootable JAR
RUN chmod +x gradlew
RUN ./gradlew clean bootJar --no-daemon

# Stage 2: Runtime (Hardened & Lightweight)
FROM eclipse-temurin:21-jre-alpine

# Create a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar
RUN chown appuser:appgroup app.jar

# Run as non-root user
USER appuser

EXPOSE 8080

# Health check using Spring Boot's liveness probe (JVM up), not the aggregate
# /actuator/health endpoint — that one also reflects Mongo status, which would
# keep the container "unhealthy" (and the deploy stuck) whenever the database
# is unreachable, even though the app itself is running fine.
HEALTHCHECK --interval=30s --timeout=5s --retries=3 --start-period=40s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT:-8080}/actuator/health/liveness || exit 1

# Optimize JVM settings for container environment
ENTRYPOINT ["java", "-XX:+UseZGC", "-XX:MaxRAMPercentage=75", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
