FROM gradle:9.2.1-jdk21

WORKDIR /home/gradle/niffler
ENV TZ=Europe/Moscow
COPY ./niffler-e-2-e-tests ./niffler-e-2-e-tests
COPY ./niffler-grpc-common ./niffler-grpc-common
COPY ./build.gradle ./
COPY ./settings.gradle ./
COPY ./gradle.properties ./

CMD gradle test -p niffler-e-2-e-tests -Dtest.env=docker -Drepository=jpa -Duser.timezone=Europe/Moscow
