#STAGE 1 : Build the JAR file

FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /pixxy-notification-service
COPY pom.xml .
COPY /src src
RUN mvn clean package -DskipTests


# Running the jar

FROM amazoncorretto:21-alpine3.22-jdk
WORKDIR /pixxy-notification-service
COPY --from=build /pixxy-notification-service/target/pixxy-notification-service.jar .
ENTRYPOINT [ "java", "-jar","pixxy-notification-service.jar" ]