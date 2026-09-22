FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Creates the /app directory inside the image and sets it as the working directory
COPY target/*.jar app.jar
# Copies the built jar from the host into /app/*.jar inside the image
ENTRYPOINT ["java", "-jar", "app.jar"]
# The command executed when the container starts