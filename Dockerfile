FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY build/libs/wallet*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/app.jar"]
