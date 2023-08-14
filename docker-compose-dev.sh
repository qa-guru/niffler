#!/bin/bash

echo '### Java version ###'
java --version
echo '### Gradle version ###'
gradle --version

front=""
front_image=""
if [[ "$1" = "gql" ]]; then
  front="./niffler-frontend-gql/";
  front_image="dtuchs/niffler-frontend-gql:latest";
else
  front="./niffler-frontend/";
  front_image="dtuchs/niffler-frontend:latest";
fi

FRONT_IMAGE="$front_image" docker-compose down

docker_containers="$(docker ps -a -q)"
docker_images="$(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'niffler')"

if [ ! -z "$docker_containers" ]; then
  echo "### Stop containers: $docker_containers ###"
  docker stop $(docker ps -a -q)
  docker rm $(docker ps -a -q)
fi
if [ ! -z "$docker_images" ]; then
  echo "### Remove images: $docker_images ###"
  docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'niffler')
fi

if [ "$1" = "push" ] || [ "$2" = "push" ]; then
  echo "### Build & push images (front: $front) ###"
  bash ./gradlew clean build dockerPush -x :niffler-e-2-e-tests:test
  cd "$front" || exit
  bash ./docker-build.sh dev push
else
  echo "### Build images (front: $front) ###"
  bash ./gradlew clean build dockerTagLatest -x :niffler-e-2-e-tests:test
  cd "$front" || exit
  bash ./docker-build.sh dev
fi

cd ../
docker images
FRONT_IMAGE="$front_image" docker-compose up -d
docker ps -a
