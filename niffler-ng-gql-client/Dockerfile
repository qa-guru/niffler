FROM node:22.6.0-alpine AS build
ARG NPM_COMMAND
ARG VERSION
WORKDIR /app
COPY package.json ./
RUN npm install
COPY . ./
RUN npm run ${NPM_COMMAND}
 
# release step
FROM nginx:1.27.1-alpine AS release
LABEL maintainer="Irina Stiazhkina @irin_alexis"
LABEL version=${VERSION}
ENV TZ=Europe/Moscow
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html/
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
