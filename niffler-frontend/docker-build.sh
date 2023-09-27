#!/bin/bash
source ../docker.properties

if [ "$1" = "docker" ]; then
  echo '### Build dev frontend image ###'
  docker build --build-arg NPM_COMMAND=${NPM_DOCKER_BUILD} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-"$1":${FRONT_VERSION} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-"$1":latest .
elif [ "$1" = "staging" ]; then
  echo '### Build staging frontend image ###'
  docker build --build-arg NPM_COMMAND=${NPM_STAGING_BUILD} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-"$1":${FRONT_VERSION} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-"$1":latest .
elif [ "$1" = "prod" ]; then
  echo '### Build prod frontend image ###'
  docker build --build-arg NPM_COMMAND=${NPM_PROD_BUILD} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-"$1":${FRONT_VERSION} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-"$1":latest .
fi

if [ "$2" = "push" ]; then
  echo '### Push frontend image ###'
  docker push ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-"$1":${FRONT_VERSION}
  docker push ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-"$1":latest
fi
