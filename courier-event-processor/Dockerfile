FROM openjdk:11-jdk-slim

EXPOSE 8081

ADD target/*.jar /

ADD run.sh /

ADD pom.xml /pom.xml

ENTRYPOINT ["sh", "/run.sh"]