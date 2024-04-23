FROM amazoncorretto:11-alpine

ARG JAR_FILE
COPY ${JAR_FILE} /app.jar

ENV JAVA_TOOL_OPTIONS=""

ENTRYPOINT [ "/usr/bin/java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar" ]