#STAGE 1 : Build the JAR file

FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /pixxy-api-gateway
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests


# Running the jar

FROM amazoncorretto:21-alpine3.22-jdk
WORKDIR /pixxy-api-gateway
COPY --from=build /pixxy-api-gateway/target/pixxy-api-gateway.jar .
ENTRYPOINT [ "java", "-jar","pixxy-api-gateway.jar" ]