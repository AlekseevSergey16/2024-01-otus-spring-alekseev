FROM amazoncorretto:17-alpine3.19
COPY ./target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
