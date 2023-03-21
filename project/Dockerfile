FROM eclipse-temurin:19-jdk-alpine
VOLUME /tmp
COPY target/*.jar project-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/project-0.0.1-SNAPSHOT.jar"]