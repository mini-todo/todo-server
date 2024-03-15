FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY build/libs/*.jar todo-server.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/todo-server.jar"]
