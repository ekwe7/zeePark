FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY build/libs/zeePark-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "app.jar"]
