#!/bin/bash
source ./docker.properties
export PROFILE=docker

echo '### Java version ###'
java --version

front=""
front_image=""
docker_arch=""
if [[ "$1" = "gql" ]]; then
  front="./niffler-frontend-gql/";
  front_image="${IMAGE_PREFIX}/${FRONT_IMAGE_NAME_GQL}-${PROFILE}:latest";
else
  front="./niffler-frontend/";
  front_image="$IMAGE_PREFIX/${FRONT_IMAGE_NAME}-${PROFILE}:latest";
fi

ARCH="$docker_arch" FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" PROFILE="${PROFILE}" docker-compose -f docker-compose.test.yml down

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

ARCH=$(uname -m)

bash ./gradlew -Pskipjaxb jibDockerBuild -x :niffler-e-2-e-tests:test

if [ "$ARCH" = "arm64" ] || [ "$ARCH" = "aarch64" ]; then
  docker_arch="linux/arm64"
  docker build --build-arg DOCKER=arm64v8/eclipse-temurin:19-jdk -t "${IMAGE_PREFIX}/${TEST_IMAGE_NAME}":"${VERSION}" -t "${IMAGE_PREFIX}/${TEST_IMAGE_NAME}":latest -f ./niffler-e-2-e-tests/Dockerfile .
else
  docker_arch="linux/amd64"
  docker build --build-arg DOCKER=eclipse-temurin:19-jdk -t "${IMAGE_PREFIX}/${TEST_IMAGE_NAME}":"${VERSION}" -t "${IMAGE_PREFIX}/${TEST_IMAGE_NAME}":latest -f ./niffler-e-2-e-tests/Dockerfile .
fi

cd "$front" || exit
bash ./docker-build.sh ${PROFILE}
cd ../ || exit
docker pull selenoid/vnc_chrome:116.0
docker images
ARCH="$docker_arch" FRONT_IMAGE="$front_image" PREFIX="${IMAGE_PREFIX}" PROFILE="${PROFILE}" docker-compose -f docker-compose.test.yml up -d
docker ps -a
