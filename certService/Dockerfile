FROM docker.io/openjdk:11-jre-slim

ARG JAR_FILE_PATH=target/cert-service-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE_PATH} ./app/cert-service.jar

ENTRYPOINT ["java","-jar","./app/cert-service.jar"]
