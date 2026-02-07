#STAGE 1 : Build the JAR file

FROM maven:3.9-eclipse-temurin-21-alpine AS build
ARG GITHUB_USER
ARG GITHUB_TOKEN
WORKDIR /pixxy-user-service
RUN mkdir -p /root/.m2 && \
    printf '<settings><servers><server><id>github</id><username>%s</username><password>%s</password></server></servers></settings>' \
    "$GITHUB_USER" "$GITHUB_TOKEN" > /root/.m2/settings.xml
COPY pom.xml .
COPY src src
# COPY settings.xml /root/.m2/settings.xml
RUN mvn clean package -DskipTests


# Running the jar

FROM amazoncorretto:21-alpine3.22-jdk
WORKDIR /pixxy-user-service
COPY --from=build /pixxy-user-service/target/pixxy-user-service.jar .
ENTRYPOINT [ "java", "-jar","pixxy-user-service.jar" ]