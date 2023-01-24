FROM openjdk/openjdk:17-alpine
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
COPY ./build/libs/niffler-gateway-$VER.jar /usr/src/app/
WORKDIR /usr/src/app

ENV JPDA_ADDRESS=8787
ENV JPDA_TRANSPORT=dt_socket

EXPOSE 8787
EXPOSE 8090
CMD java $JAVA_OPTS -jar niffler-gateway-$VER.jar jpda