#!/bin/bash
source ./docker.properties

docker build --build-arg NPM_COMMAND=${TEST_BUILD} -t ${IMAGE_NAME}-test:${VERSION} -t ${IMAGE_NAME}-test:latest .
