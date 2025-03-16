# start
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
ENV PORT=8080
EXPOSE PORT

ENTRY POINT ["java", "-jar", "app.jar"]
# end