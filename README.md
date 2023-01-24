## **Технологии, использованные в Niffler**
- [Spring Authorization Server](https://spring.io/projects/spring-authorization-server)
- [Spring OAuth 2.0 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [Spring data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Web](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#spring-web)
- [Spring gRPC by https://github.com/yidongnan](https://yidongnan.github.io/grpc-spring-boot-starter/en/server/getting-started.html)
- [Docker](https://www.docker.com/resources/what-container/)
- [Postgres](https://www.postgresql.org/about/)
- [React](https://ru.reactjs.org/docs/getting-started.html)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Jakarta Bean Validation](https://beanvalidation.org/)

Рекомендуемые материалы к просмотру:

- [Implementing an OAuth 2 authorization server with Spring Security - the new way! by Laurentiu Spilca](https://youtu.be/DaUGKnA7aro)
- [Spring Data? Да, та! by Евгений Борисов](https://youtu.be/nwM7A4TwU3M)
- [Перенимаем опыт Google в построении микросервисов с gRPC by Александр Борисов](https://youtu.be/zPbaKUIcFx0)




**Минимальные предусловия для работы с проектом Niffler**

#### 1. Установить docker (Если не установлен)
Мы будем использовать docker для БД (Postgres), кроме того, будем запускать микросервисы в едином docker network при помощи docker-compose

[Установка на Windows](https://docs.docker.com/desktop/install/windows-install/)

[Установка на Mac](https://docs.docker.com/desktop/install/mac-install/) (Для ARM и Intel разные пакеты)

[Установка на Linux](https://docs.docker.com/desktop/install/linux-install/)

После установки и запуска docker daemon необходимо убедиться в работе команд docker, например `docker -v`:
```posh
Dmitriis-MacBook-Pro ~ % docker -v
Docker version 20.10.14, build a224086
```
#### 2. Спуллить контейнер postgres версии 15.1
```posh
docker pull postgres:15.1
```
После `pull` вы увидите спуленный image командой `docker images`

```posh
mitriis-MacBook-Pro ~ % docker images            
REPOSITORY                 TAG       IMAGE ID       CREATED        SIZE
postgres                   15.1      9f3ec01f884d   10 days ago    379MB
postgres                   latest    9f3ec01f884d   10 days ago    379MB
```
#### 3. Создать volume для сохранения данных из БД в docker на вашем компьютере
```posh
docker volume create pgdata
```
#### 4. Запустить БД командой 
```posh
docker run --name niffler-all -p 5432:5432 -e POSTGRES_PASSWORD=secret -v pgdata:/var/lib/postgresql/data -d postgres:15.1
```

#### 5. Установить одну из программ для визуальной работы с Postgres
Например, DBeaver или Datagrip. Мы рекоменуем бесплатную PgAdmin 4.

#### 6. Подключиться к БД postgres (host: localhost, port: 5432, user: postgres, pass: secret, database name: postgres) из PgAdmin и создать пустые БД микросервисов
```sql
create database "niffler-userdata" with owner postgres;
create database "niffler-spend" with owner postgres;
create database "niffler-currency" with owner postgres;
create database "niffler-auth" with owner postgres;
```

#### 7. Установить Java версии 17 или новее. Это необходимо, т.к. проект не поддерживает версии <17
Версию установленной Java необходимо проверить командой `java -version`
```posh
Dmitriis-MacBook-Pro ~ % java -version
openjdk version "19.0.1" 2022-10-18
OpenJDK Runtime Environment Homebrew (build 19.0.1)
```

Если у вас несколько версий Java одновременно - то хотя бы одна из них должна быть 17+
Если java не установлена вовсе, то рекомендую установить OpenJDK (например, из https://adoptium.net/en-GB/)

#### 8. Установить пакетый менеджер для сборки front-end npm
[Инструкция](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm).
Рекомендованная версия Node.js - 18.13.0 (LTS)

#### 9. Прописать в etc/hosts элиас auth-server 127.0.0.1
```posh
Dmitriis-MacBook-Pro niffler % vi /etc/hosts
```
```posh
##
# Host Database
#
# localhost is used to configure the loopback interface
# when the system is booting.  Do not change this entry.
##
127.0.0.1       localhost
127.0.0.1       auth-server
```

#### 10. Запустить фронтенд (сначала обновить зависимости)
```posh
Dmitriis-MacBook-Pro niffler % cd niffler-frontend
Dmitriis-MacBook-Pro niffler-frontend % npm i
Dmitriis-MacBook-Pro niffler-frontend % npm start
```

# Запуск Niffler локально:
- Запустить сервис auth
```posh
Dmitriis-MacBook-Pro niffler % cd niffler-auth
Dmitriis-MacBook-Pro niffler-auth % gradle bootRun
```
или просто перейдя к main-классу приложения NifflerAuthApplication выбрать run в IDEA
- Запустить в любой последовательности другие сервисы: niffler-currency, niffler-spend, niffler-gateway, niffler-userdata


![Enjoy the Niffler](/niffler-frontend/public/images/niffler-logo.png)

