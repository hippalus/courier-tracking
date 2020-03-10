FROM openjdk:11-jdk-slim

ADD target/*.jar /

ADD pom.xml /pom.xml

RUN echo $(ls -1 /)

EXPOSE 8081

ENTRYPOINT [ "java", "-jar", "courier-event-processor-1.0.jar"]