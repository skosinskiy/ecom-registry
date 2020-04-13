# This docker file is only for production.

# Build from jdk image
FROM openjdk:11

# Copy fat jar into container
ADD /target/*.jar app.jar

# Run spring boot application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]

