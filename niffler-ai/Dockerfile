FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/niffler-ai-*.jar app.jar
EXPOSE 8094
ENTRYPOINT ["java", "-jar", "app.jar"]

