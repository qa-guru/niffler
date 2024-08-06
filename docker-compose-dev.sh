#!/bin/bash
source ./docker.properties
export PROFILE=docker
export PREFIX="${IMAGE_PREFIX}"
export FRONT_VERSION="${FRONT_VERSION}"
export ARCH=$(uname -m)

echo '### Java version ###'
java --version

if [[ "$1" = "gql" ]]; then
  export FRONT="niffler-frontend-gql"
else
  export FRONT="niffler-frontend"
fi

docker compose down

docker_containers=$(docker ps -a -q)
docker_images=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'niffler')

if [ ! -z "$docker_containers" ]; then
  echo "### Stop containers: $docker_containers ###"
  docker stop $docker_containers
  docker rm $docker_containers
fi

if [ ! -z "$docker_images" ]; then
  echo "### Remove images: $docker_images ###"
  docker rmi $docker_images
fi

if [ "$1" = "push" ] || [ "$2" = "push" ]; then
  echo "### Build & push images ###"
  bash ./gradlew -Pskipjaxb jib -x :niffler-e-2-e-tests:test
  docker compose push frontend.niffler.dc
else
  echo "### Build images ###"
  bash ./gradlew -Pskipjaxb jibDockerBuild -x :niffler-e-2-e-tests:test
fi

docker compose up -d
docker ps -a
