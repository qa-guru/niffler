FROM openjdk/openjdk:17-alpine
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
COPY ./build/libs/niffler-currency-$VER.jar /usr/src/app/
WORKDIR /usr/src/app

ENV JPDA_ADDRESS=8787
ENV JPDA_TRANSPORT=dt_socket

EXPOSE 8787
EXPOSE 8091
EXPOSE 8092
CMD java $JAVA_OPTS -jar niffler-currency-$VER.jar jpda