FROM gradle:9.2.1-jdk21

WORKDIR /home/gradle/niffler
ENV TZ=Europe/Moscow

COPY ./build.gradle ./
COPY ./settings.gradle ./
COPY ./gradle.properties ./
COPY ./niffler-e-2-e-tests/build.gradle ./niffler-e-2-e-tests/
COPY ./niffler-grpc-common/build.gradle ./niffler-grpc-common/

RUN gradle :niffler-e-2-e-tests:dependencies :niffler-grpc-common:dependencies --no-daemon

COPY ./niffler-e-2-e-tests ./niffler-e-2-e-tests
COPY ./niffler-grpc-common ./niffler-grpc-common

CMD gradle test -p niffler-e-2-e-tests --no-daemon -Dtest.env=docker -Drepository=jpa -Duser.timezone=Europe/Moscow
