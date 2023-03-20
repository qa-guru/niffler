source ./docker.properties

docker build --build-arg APP_VER=${VERSION} -t ${IMAGE_NAME}:${VERSION} -t ${IMAGE_NAME}:latest .

docker push ${IMAGE_NAME}:${VERSION}
docker push ${IMAGE_NAME}:latest
