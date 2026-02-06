#STAGE 1 : Build the JAR file

FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /pixxy-file-service
COPY pom.xml .
COPY src src
COPY settings.xml /root/.m2/settings.xml
RUN mvn clean package -DskipTests


# Running the jar

FROM amazoncorretto:21-alpine3.22-jdk
WORKDIR /pixxy-file-service
COPY --from=build /pixxy-file-service/target/pixxy-file-service.jar .
ENTRYPOINT [ "java", "-jar","pixxy-file-service.jar" ]