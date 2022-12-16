FROM amazoncorretto:17-alpine-jdk
COPY application/build/libs/application-0.0.1-SNAPSHOT.jar application-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/application-0.0.1-SNAPSHOT.jar"]