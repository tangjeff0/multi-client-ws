FROM openjdk:8-alpine

COPY target/uberjar/multi-client-ws.jar /multi-client-ws/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/multi-client-ws/app.jar"]
