#STAGE 1 : Build the JAR file

FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /pixxy-user-service
COPY pom.xml .
COPY src src
COPY settings.xml /root/.m2/settings.xml
RUN mvn clean package -DskipTests


# Running the jar

FROM amazoncorretto:21-alpine3.22-jdk
WORKDIR /pixxy-user-service
COPY --from=build /pixxy-user-service/target/pixxy-user-service.jar .
ENTRYPOINT [ "java", "-jar","pixxy-user-service.jar" ]