FROM openjdk:17-alpine
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY niffler-currency-$VER.jar ./

ENV JPDA_ADDRESS=8787
ENV JPDA_TRANSPORT=dt_socket

EXPOSE 8787
EXPOSE 8091
EXPOSE 8092
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS niffler-currency-$VER.jar jpda