FROM gradle:7.6.1-jdk17-alpine
ENV JAVA_OPTS=""
RUN apk add tree
RUN apk add gcompat
WORKDIR /niffler
COPY ./niffler-e-2-e-tests ./niffler-e-2-e-tests
COPY ./niffler-grpc-common ./niffler-grpc-common
COPY ./build.gradle ./
COPY ./settings.gradle ./

CMD tree; gradle test -Dtest.env=docker
