FROM eclipse-temurin:21
WORKDIR /app
COPY ./target/weather_task-2025.1.1.jar /app/weather_task-2025.1.1.jar
ENTRYPOINT ["java", "-jar", "weather_task-2025.1.1.jar", "--spring.config.location=classpath:./application.properties,file:./application.yml" ]