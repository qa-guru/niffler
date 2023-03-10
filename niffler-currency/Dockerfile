ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY niffler-currency-$VER.jar ./

EXPOSE 8091
EXPOSE 8092
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS niffler-currency-$VER.jar
