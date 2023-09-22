#!/bin/bash

if [[ "$1" = "prod" ]] || [[ "$2" = "prod" ]]; then export PROFILE=prod; else export PROFILE=staging; fi
var front
if [[ "$1" = "gql" ]]; then front="./niffler-frontend-gql/"; else front="./niffler-frontend/"; fi

echo "### Build & push images for profile ${PROFILE}, (front: ${front}) ###"
bash ./gradlew -Pskipjaxb jib -x :niffler-e-2-e-tests:test
cd "$front" || exit
bash ./docker-build.sh ${PROFILE}

cd ../
