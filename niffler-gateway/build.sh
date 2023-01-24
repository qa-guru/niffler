source ./docker.properties

echo DEB - ${VERSION}

# Build project
gradle build

# Build docker image
docker build --build-arg APP_VER=${VERSION} -t ${IMAGE_NAME}:${VERSION} -t ${IMAGE_NAME}:latest .
