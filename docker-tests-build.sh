#!/bin/bash
source ./niffler-e-2-e-tests/docker.properties

docker build -t ${IMAGE_NAME}:${VERSION} -t ${IMAGE_NAME}:latest -f ./niffler-e-2-e-tests/Dockerfile .
