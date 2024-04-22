#!/bin/bash
source ./docker.properties
export PROFILE="${PROFILE:=docker}"

echo '### Java version ###'
java --version

front=""
front_image=""
if [[ "$1" = "gql" ]]; then
  front="./niffler-frontend-gql/";
  front_image="${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}-${PROFILE}:latest";
else
  front="./niffler-frontend/";
  front_image="${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-${PROFILE}:latest";
fi

FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" PROFILE="${PROFILE}" docker-compose down

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
  bash ./gradlew --stacktrace -Pskipjaxb jib -x :niffler-e-2-e-tests:test || exit 1
  cd "$front" || exit
  bash ./docker-build.sh ${PROFILE} push || exit 1
else
  echo "### Build images (front: $front) ###"
  bash ./gradlew --stacktrace -Pskipjaxb jibDockerBuild -x :niffler-e-2-e-tests:test || exit 1
  cd "$front" || exit
  bash ./docker-build.sh ${PROFILE} || exit 1
fi

cd ../
docker images
FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" PROFILE="${PROFILE}" docker-compose up -d
docker ps -a
