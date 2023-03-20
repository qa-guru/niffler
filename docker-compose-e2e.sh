#!/bin/bash

source ./niffler-e-2-e-tests/docker.properties

echo '### Java version ###'
java --version
echo '### Gradle version ###'
gradle --version

docker-compose -f docker-compose.test.yml down
#docker stop $(docker ps -a -q)
#docker rm $(docker ps -a -q)
docker ps -a
docker rmi -f $(docker images | grep 'niffler')

ARCH=$(uname -m)

bash ./gradlew clean build dockerTagLatest -x :niffler-e-2-e-tests:test

if [ "$ARCH" = "arm64" ] || [ "$ARCH" = "aarch64" ]; then
  docker build --build-arg DOCKER=arm64v8/eclipse-temurin:19-jdk -t "${IMAGE_NAME}":"${VERSION}" -t "${IMAGE_NAME}":latest -f ./niffler-e-2-e-tests/Dockerfile .
else
  docker build --build-arg DOCKER=eclipse-temurin:19-jdk -t "${IMAGE_NAME}":"${VERSION}" -t "${IMAGE_NAME}":latest -f ./niffler-e-2-e-tests/Dockerfile .
fi

var front
if [[ "$1" = "gql" ]]; then front="./niffler-frontend-gql/"; else front="./niffler-frontend/"; fi

cd "$front" || exit
bash ./docker-build.sh test
cd ../ || exit
docker pull selenoid/vnc_chrome:110.0
docker images
docker-compose -f docker-compose.test.yml up -d
docker ps -a
