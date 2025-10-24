FROM eclipse-temurin:21-jre
COPY target/capital-gain-jar-with-dependencies.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]