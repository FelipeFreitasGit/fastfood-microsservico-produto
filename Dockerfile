FROM amazoncorretto:17-alpine-jdk

COPY /build/libs/*.jar api.jar

EXPOSE 8084

ENTRYPOINT exec java $JAVA_OPTS -jar /api.jar