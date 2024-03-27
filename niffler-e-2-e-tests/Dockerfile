ARG DOCKER
ARG ALLURE_DOCKER_API
ARG GITHUB_TOKEN

FROM ${DOCKER}

ENV ALLURE_DOCKER_API=${ALLURE_DOCKER_API}
ENV GITHUB_TOKEN=${GITHUB_TOKEN}

WORKDIR /niffler
COPY ./gradle ./gradle
COPY ./gradlew ./
COPY ./niffler-e-2-e-tests ./niffler-e-2-e-tests
COPY ./niffler-grpc-common ./niffler-grpc-common
COPY ./build.gradle ./
COPY ./settings.gradle ./

CMD ./gradlew test -Dtest.env=docker -Drepository=jpa
