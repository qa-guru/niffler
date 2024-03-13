ARG DOCKER
FROM $DOCKER
ENV JAVA_OPTS=""

WORKDIR /niffler
COPY ./gradle ./gradle
COPY ./gradlew ./
COPY ./niffler-e-2-e-tests ./niffler-e-2-e-tests
COPY ./niffler-grpc-common ./niffler-grpc-common
COPY ./build.gradle ./
COPY ./settings.gradle ./

CMD ./gradlew test -Dtest.env=docker -Drepository=jpa
