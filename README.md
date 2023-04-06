## **Технологии, использованные в Niffler**

- [Spring Authorization Server](https://spring.io/projects/spring-authorization-server)
- [Spring OAuth 2.0 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [Spring data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Web](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#spring-web)
- [Spring actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Spring gRPC by https://github.com/yidongnan](https://yidongnan.github.io/grpc-spring-boot-starter/en/server/getting-started.html)
- [Spring web-services](https://docs.spring.io/spring-ws/docs/current/reference/html/)
- [Docker](https://www.docker.com/resources/what-container/)
- [Docker-compose](https://docs.docker.com/compose/)
- [Postgres](https://www.postgresql.org/about/)
- [React](https://ru.reactjs.org/docs/getting-started.html)
- [GraphQL](https://graphql.org/)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Jakarta Bean Validation](https://beanvalidation.org/)
- [Jakarta JAXB](https://eclipse-ee4j.github.io/jaxb-ri/)
- [JUnit 5 (Extensions, Resolvers, etc)](https://junit.org/junit5/docs/current/user-guide/)
- [Retrofit 2](https://square.github.io/retrofit/)
- [Allure](https://docs.qameta.io/allure/)
- [Selenide](https://selenide.org/)
- [Selenoid & Selenoid-UI](https://aerokube.com/selenoid/latest/)
- [Allure-docker-service](https://github.com/fescobar/allure-docker-service)
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Gradle 7.6](https://docs.gradle.org/7.6/release-notes.html)
- And much more:)

Рекомендуемые материалы к просмотру:

- [Implementing an OAuth 2 authorization server with Spring Security - the new way! by Laurentiu Spilca](https://youtu.be/DaUGKnA7aro)
- [Spring Data? Да, та! by Евгений Борисов](https://youtu.be/nwM7A4TwU3M)
- [Перенимаем опыт Google в построении микросервисов с gRPC by Александр Борисов](https://youtu.be/zPbaKUIcFx0)

**Минимальные предусловия для работы с проектом Niffler**

#### 1. Установить docker (Если не установлен)

Мы будем использовать docker для БД (Postgres), кроме того, будем запускать микросервисы в едином docker network при
помощи docker-compose

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

Например, DBeaver или Datagrip. Мы рекомендуем бесплатную PgAdmin 4.

#### 6. Подключиться к БД postgres (host: localhost, port: 5432, user: postgres, pass: secret, database name: postgres) из PgAdmin и создать пустые БД микросервисов

```sql
create
database "niffler-userdata" with owner postgres;
create
database "niffler-spend" with owner postgres;
create
database "niffler-currency" with owner postgres;
create
database "niffler-auth" with owner postgres;
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

# Запуск Niffler локальное в IDE:

#### 1. Выбрать какой фронтенд предполагается запускать - REST или GraphQL, и перейти в соответсвующий каталог

для REST:

```posh
Dmitriis-MacBook-Pro niffler % cd niffler-frontend
```

или для GraphQL:

```posh
Dmitriis-MacBook-Pro niffler % cd niffler-frontend-gql
```

#### 2. Запустить фронтенд (сначала обновить зависимости)

```posh
Dmitriis-MacBook-Pro niffler-frontend % npm i
Dmitriis-MacBook-Pro niffler-frontend % npm run build:dev
```

#### 3. Прописать run конфигурацию для всех сервисов niffler-* - Active profiles local

Для этого зайти в меню Run -> Edit Configurations -> выбрать main класс -> указать Active profiles: local
[Инструкция](https://stackoverflow.com/questions/39738901/how-do-i-activate-a-spring-boot-profile-when-running-from-intellij).

#### 4 Запустить сервис Niffler-auth c помощью gradle или командой Run в IDE:
- 

- Запустить сервис auth

```posh
Dmitriis-MacBook-Pro niffler % cd niffler-auth
Dmitriis-MacBook-Pro niffler-auth % gradle bootRun --args='--spring.profiles.active=local'
```

Или просто перейдя к main-классу приложения NifflerAuthApplication выбрать run в IDEA (предварительно удостовериться что
выполнен предыдущий пункт)

#### 5  Запустить в любой последовательности другие сервисы: niffler-currency, niffler-spend, niffler-gateway, niffler-userdata

# Запуск Niffler в докере:

#### 1. Создать бесплатную учетную запись на https://hub.docker.com/ (если отсутствует)

#### 2. Создать в настройках своей учетной записи access_token

[Инструкция](https://docs.docker.com/docker-hub/access-tokens/).

#### 3. Выполнить docker login с созданным access_token (в инструкции это описано)

#### 4. Прописать в etc/hosts элиас для Docker-имени фронтенда:  127.0.0.1 niffler-frontend

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
127.0.0.1       niffler-frontend
```

#### 5. Перейти в корневой каталог проекта

```posh
Dmitriis-MacBook-Pro niffler % cd niffler
```

#### 6. Запустить все сервисы, если необходим фронтенд GraphQL, то это указывается аргументом к скрипту:

для REST:

```posh
Dmitriis-MacBook-Pro  niffler % bash docker-compose-dev.sh
```

для GraphQL:

```posh
Dmitriis-MacBook-Pro  niffler % bash docker-compose-dev.sh gql
```

Niffler при запуске в докере будет работать для вас по адресу http://niffler-frontend:80/, этот порт НЕ НУЖНО указывать
в браузере, таким образом переходить напрямую по ссылке http://niffler-frontend/
*ВАЖНО!* из docker-network Вам будут доступны только следующие порты:

- порт 80 (все запросы с него перенаправляются nginx-ом на frontend)
- порт 9000 (сервис niffler-auth)
- порт 8090 (сервис niffler-gateway)

# Создание своего docker repository для форка Niffler и сборка своих докер контейнеров

#### 1. Войти в свою УЗ на https://hub.docker.com/ и последовательно создать публичные репозитории

- niffler-frontend
- niffler-frontend-gql
- niffler-userdata
- niffler-spend
- niffler-gateway
- niffler-currency
- niffler-auth

Допустим, что ваш username на https://hub.docker.com - *foobazz*

#### 2. заменить в проекте все имена image dtuchs/niffler на foobazz/niffler

- где foobazz - ваш юзернэйм на https://hub.docker.com/

!К замене надо отнестись внимательно, вот список мест на текущий момент:!

- build.gradle всех сервисов Spring
- docker-compose.yaml в корне проекта
- docker-compose.test.yaml в корне проекта
- docker.properties в модуле niffler-frontend
- docker.properties в модуле niffler-frontend-gql

#### 3. Перейти в корневой каталог проекта

```posh
Dmitriis-MacBook-Pro niffler % cd niffler
```

#### 4. Собрать все имеджи, запушить и запустить niffler одной командой, если необходим фронтенд GraphQL, то это указывается аргументом к скрипту:

для REST:

```posh
Dmitriis-MacBook-Pro  niffler % bash docker-compose-dev.sh push
```

для GraphQL:

```posh
Dmitriis-MacBook-Pro  niffler % bash docker-compose-dev.sh gql push
```

# Запуск e-2-e тестов в Docker network изолированно Niffler в докере:

#### 1. Перейти в корневой каталог проекта

```posh
Dmitriis-MacBook-Pro niffler % cd niffler
```

#### 2. Запустить все сервисы и тесты, если необходим фронтенд GraphQL, то это указывается аргументом к скрипту:

для REST:

```posh
Dmitriis-MacBook-Pro  niffler % bash docker-compose-e2e.sh
```

для GraphQL:

```posh
Dmitriis-MacBook-Pro  niffler % bash docker-compose-e2e.sh gql
```

#### 3. Selenoid UI доступен по адресу: http://localhost:9090/

#### 4. Allure доступен по адресу: http://localhost:5050/allure-docker-service/projects/niffler-e-2-e-tests/reports/latest/index.html

![Enjoy the Niffler](/niffler-frontend/public/images/niffler-logo.png)