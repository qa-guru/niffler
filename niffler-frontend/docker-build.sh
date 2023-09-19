#!/bin/bash
source ../docker.properties

if [ "$1" = "dev" ]; then
  echo '### Build dev frontend image ###'
  docker build --build-arg NPM_COMMAND=${NPM_DEV_BUILD} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}:${VERSION} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}:latest .
elif [ "$1" = "test" ]; then
  echo '### Build test frontend image ###'
  docker build --build-arg NPM_COMMAND=${NPM_TEST_BUILD} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-test:${VERSION} -t ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}-test:latest .
fi

if [ "$2" = "push" ]; then
  echo '### Push frontend image ###'
  docker push ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}:${VERSION}
  docker push ${IMAGE_PREFIX}/${FRONT_IMAGE_NAME}:latest
fi
