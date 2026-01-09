FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/BarakaPay-0.0.1-SNAPSHOT.jar app.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]