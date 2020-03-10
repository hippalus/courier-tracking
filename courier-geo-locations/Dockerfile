FROM openjdk:11-jdk-slim

ADD target/*.jar /

ADD data/* ./courier-geo-locations/data/

ADD pom.xml /pom.xml

RUN echo $(ls -1 /)

ENTRYPOINT [ "java", "-jar", "courier-geo-locations-1.0.jar"]