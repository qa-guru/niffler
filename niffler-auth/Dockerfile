#ARG DOCKER
FROM eclipse-temurin:19-jdk
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY niffler-auth-$VER.jar ./
  
EXPOSE 9000
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS niffler-auth-$VER.jar
