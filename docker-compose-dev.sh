#!/bin/bash
source ./docker.properties
export PROFILE=docker
export PREFIX="${IMAGE_PREFIX}"

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

echo '### Java version ###'
java --version
bash ./gradlew clean
if [ "$1" = "push" ]; then
  echo "### Build & push images ###"
  bash ./gradlew jib -x :niffler-e-2-e-tests:test
  docker compose push frontend.niffler.dc
else
  echo "### Build images ###"
  bash ./gradlew jibDockerBuild -x :niffler-e-2-e-tests:test
fi

docker compose up -d
docker ps -a
