FROM openjdk/openjdk:17-alpine
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
COPY ./build/libs/niffler-userdata-$VER.jar /usr/src/app/
WORKDIR /usr/src/app

ENV JPDA_ADDRESS=8787
ENV JPDA_TRANSPORT=dt_socket

EXPOSE 8787
EXPOSE 8089
CMD java $JAVA_OPTS -jar niffler-userdata-$VER.jar jpda