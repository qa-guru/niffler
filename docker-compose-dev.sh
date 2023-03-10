#!/bin/bash

echo '### Java version ###'
java --version
echo '### Gradle version ###'
gradle --version

docker-compose down
#docker stop $(docker ps -a -q)
#docker rm $(docker ps -a -q)
docker ps -a
docker rmi -f $(docker images | grep 'niffler')

if [ "$1" = "push" ]; then
  echo '### Build & push images ###'
  bash ./gradlew clean build dockerPush -x :niffler-e-2-e-tests:test
  cd ./niffler-frontend/ || exit
  bash ./docker-build.sh dev "$1"
else
  echo '### Build images without pushing ###'
  bash ./gradlew clean build dockerTagLatest -x :niffler-e-2-e-tests:test
  cd ./niffler-frontend/ || exit
  bash ./docker-build.sh dev "$1"
fi

cd ../
docker images
docker-compose up -d
docker ps -a
