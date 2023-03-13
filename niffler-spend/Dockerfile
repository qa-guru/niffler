ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY niffler-spend-$VER.jar ./

EXPOSE 8093
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS niffler-spend-$VER.jar
