#!/bin/bash
source ./docker.properties

docker build --build-arg NPM_COMMAND=${DEV_BUILD} -t ${IMAGE_NAME}:${VERSION} -t ${IMAGE_NAME}:latest .

docker push ${IMAGE_NAME}:${VERSION}
docker push ${IMAGE_NAME}:latest
