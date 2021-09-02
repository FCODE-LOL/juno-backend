FROM openjdk:11
EXPOSE 8083
COPY target/juno-0.0.1-SNAPSHOT.jar juno-0.0.1.jar
ENTRYPOINT ["java","-jar","juno-0.0.1.jar"]
