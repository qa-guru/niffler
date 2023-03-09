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
docker rmi -f "$(docker images | grep 'niffler')"
bash ./gradlew clean build dockerTag -x :niffler-e-2-e-tests:test
docker build -t "${IMAGE_NAME}":"${VERSION}" -t "${IMAGE_NAME}":latest -f ./niffler-e-2-e-tests/Dockerfile .
cd ./niffler-frontend/ || exit
bash ./docker-build.sh test
cd ../
docker pull selenoid/vnc:chrome_110.0
docker images
docker-compose -f docker-compose.test.yml up -d
docker ps -a
