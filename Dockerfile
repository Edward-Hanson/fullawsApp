FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/cloudbasedproject.jar app.jar

EXPOSE 2020

ENTRYPOINT ["java", "-jar", "app.jar"]
