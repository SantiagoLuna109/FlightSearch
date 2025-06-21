FROM amazoncorretto:21-alpine

COPY build/libs/*.jar app.jar

WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]