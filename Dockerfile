#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /src
COPY checkstyle_config.xml /checkstyle_config.xml
COPY frontend/src /frontend/src
COPY frontend/public /frontend/public
COPY frontend/package.json /frontend/package.json
COPY pom.xml /pom.xml
RUN mvn -f /pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /target/*.jar app.jar

