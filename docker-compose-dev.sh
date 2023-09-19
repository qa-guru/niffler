#!/bin/bash
source ./docker.properties

echo '### Java version ###'
java --version
echo '### Gradle version ###'
gradle --version

front=""
front_image=""
if [[ "$1" = "gql" ]]; then
  front="./niffler-frontend-gql/";
  front_image="${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}:latest";
else
  front="./niffler-frontend/";
  front_image="${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}:latest";
fi

FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" docker-compose down

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
  bash ./gradlew -Pskipjaxb jib -x :niffler-e-2-e-tests:test
  cd "$front" || exit
  bash ./docker-build.sh dev push
else
  echo "### Build images (front: $front) ###"
  bash ./gradlew -Pskipjaxb jibDockerBuild -x :niffler-e-2-e-tests:test
  cd "$front" || exit
  bash ./docker-build.sh dev
fi

cd ../
docker images
FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" docker-compose up -d
docker ps -a
