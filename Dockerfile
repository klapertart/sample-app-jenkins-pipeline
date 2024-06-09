FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine-slim
ADD ./target/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java -jar $JAVA_OPTS app.jar" ]