# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Niffler is a personal finance management application built as a microservices architecture. It demonstrates OAuth 2.0 authorization, multiple communication protocols (REST, GraphQL, gRPC, SOAP), and comprehensive end-to-end testing.

## Architecture

### Microservices

- **niffler-auth** (port 9000): OAuth 2.0 Authorization Server using Spring Authorization Server
- **niffler-gateway** (port 8090): API Gateway exposing REST, GraphQL endpoints and aggregating backend services
- **niffler-currency** (port 8091 REST, 8092 gRPC): Currency exchange rates service, communicates via gRPC
- **niffler-spend** (port 8093): Spending management service, communicates via REST and gRPC
- **niffler-userdata** (port 8089): User profile and relationships service, exposes SOAP/WSDL interface
- **niffler-grpc-common**: Shared protobuf definitions and generated gRPC code

### Frontend

- **niffler-ng-client**: React + Vite frontend for REST API
- **niffler-ng-gql-client**: React + Vite frontend for GraphQL API

### Communication Patterns

- Gateway → Currency: gRPC (see [niffler-grpc-common/src/main/proto/niffler-currency.proto](niffler-grpc-common/src/main/proto/niffler-currency.proto))
- Gateway → Spend: REST
- Gateway → Userdata: Both REST and SOAP (WSDL at `/ws/userdata.wsdl`)
- Auth → Other services: Kafka messaging for user registration events
- Gateway exposes both REST API (Swagger UI) and GraphQL API (GraphiQL)

### Infrastructure

- **Postgres**: Single database container with separate schemas (niffler-auth, niffler-currency, niffler-spend, niffler-userdata)
- **Kafka + Zookeeper**: Message broker for async communication
- **Selenoid**: Browser automation infrastructure for E2E tests
- **Allure**: Test reporting

## Common Commands

### Local Development (IDE)

1. Start infrastructure:
   ```bash
   bash localenv.sh
   ```
   This starts Postgres, Zookeeper, and Kafka containers.

2. Build and run frontend (choose one):
   ```bash
   cd niffler-ng-client        # For REST frontend
   # OR
   cd niffler-ng-gql-client    # For GraphQL frontend

   npm i
   npm run build:dev           # Build and preview
   # OR
   npm run dev                 # Development mode with hot reload
   ```

3. Run backend services with Spring profile `local`:
   - Use IntelliJ run configuration with `--spring.profiles.active=local`
   - OR: `gradle bootRun --args='--spring.profiles.active=local'`
   - Start in order: niffler-auth, then niffler-currency, niffler-spend, niffler-userdata, niffler-gateway

### Docker Deployment

1. Add host entries to `/etc/hosts`:
   ```
   127.0.0.1 frontend.niffler.dc
   127.0.0.1 auth.niffler.dc
   127.0.0.1 gateway.niffler.dc
   ```

2. Run all services:
   ```bash
   bash docker-compose-dev.sh
   ```
   WARNING: This script removes ALL Docker containers. Edit the `docker rm` line if you have other containers.

3. Push images (after modifying `docker.properties` and `build.gradle` with your Docker Hub username):
   ```bash
   bash docker-compose-dev.sh push
   ```

### Testing

Run E2E tests in Docker network:
```bash
bash docker-compose-e2e.sh
```

Run specific tests locally:
```bash
cd niffler-e-2-e-tests
gradle test --tests "ClassName.testMethodName"
```

### Build Commands

Build all services:
```bash
gradle clean build
```

Build specific service:
```bash
gradle :niffler-gateway:build
```

Skip tests:
```bash
gradle build -x test
```

## Key Technical Details

### Spring Profiles

- `local`: For IDE development (services connect to localhost infrastructure)
- `docker`: For Docker Compose deployment (services use Docker network hostnames)
- `test`: For E2E test execution

### Database Migrations

Uses Flyway migrations in each service's `src/main/resources/db/migration/niffler-{service}` directory. Migrations run automatically on service startup.

### OAuth 2.0 Flow

- Authorization Server: niffler-auth (port 9000)
- Resource Server: niffler-gateway (port 8090)
- Client credentials and configuration in `application.yaml` files
- Frontend uses Authorization Code flow with PKCE

### Testing Infrastructure

- Tests located in `niffler-e-2-e-tests`
- Uses Selenide for browser automation
- Allure for test reports
- JUnit 5 with custom extensions and resolvers
- Supports REST, GraphQL, gRPC, and SOAP testing
- Database access via Spring JDBC for test data setup/teardown

## API Endpoints (when running locally)

- Frontend: http://localhost:3000/
- Gateway Swagger UI: http://localhost:8090/swagger-ui/index.html
- Gateway GraphiQL: http://localhost:8090/graphiql
- Userdata WSDL: http://localhost:8089/ws/userdata.wsdl
- Auth Server: http://localhost:9000/

When running in Docker, replace `localhost` with respective `.niffler.dc` domains and use port 80 for frontend.

## Troubleshooting

### Database Issues

If databases don't exist, manually create them:
```sql
create database "niffler-userdata" with owner postgres;
create database "niffler-spend" with owner postgres;
create database "niffler-currency" with owner postgres;
create database "niffler-auth" with owner postgres;
```

Connection: host=localhost, port=5432, user=postgres, password=secret

### Docker Issues

If Postgres container fails on Windows with "bad interpreter" error:
```bash
cd postgres/script
sed -i -e 's/\r$//' init-database.sh
chmod +x init-database.sh
```

If jib fails with credentials error, check `~/.docker/config.json` doesn't contain `"credsStore": "desktop"`.

### Port Conflicts

If port 5432 is already in use, stop other Postgres instances before running `localenv.sh`.

---

## Python E2E Tests - Roadmap to Senior Level

Current status: **Middle+ level** with solid architecture foundation.

### ✅ Already Implemented (Strong Foundation)

- **Architecture Patterns**: Builder, Repository, Page Object, API Client
- **SOLID Principles**: Single Responsibility, DRY, Immutability
- **Code Quality**: Refactoring, code smells elimination, type hints
- **Best Practices**: Model serialization (`to_dict`/`from_dict`), centralized logging

### 🎯 Senior Level Enhancement Roadmap

#### 1. Advanced Pytest Features

- [x] **Fixtures with scope management**
  - ✅ Function scope fixtures implemented (`spending_cleanup`)
  - ✅ Yield pattern for guaranteed cleanup
  - Session, module, class scopes (to be added as needed)
  - Fixture factories and parametrization (next step)
  - `autouse` fixtures for common setup (next step)

- [ ] **Parametrization (`@pytest.mark.parametrize`)**
  - Data-driven tests
  - Multiple test scenarios with single test function
  - CSV/JSON test data loading

- [ ] **Custom pytest plugins**
  - Custom markers implementation
  - Hooks (`pytest_configure`, `pytest_runtest_setup`, etc.)
  - Test result customization

- [ ] **Coverage reporting**
  - `pytest-cov` integration
  - Coverage thresholds enforcement
  - HTML/XML coverage reports

#### 2. CI/CD Integration

- [ ] **GitHub Actions / Jenkins pipelines**
  - Automated test execution on PR
  - Multi-environment test runs
  - Scheduled regression runs

- [ ] **Parallel execution (`pytest-xdist`)**
  - Distributed testing across workers
  - Test execution time optimization
  - Resource management

- [ ] **Test reports**
  - Allure report generation and publishing
  - HTML reports with screenshots
  - Test history tracking

- [ ] **Docker test containers**
  - Dockerized test environment
  - Testcontainers integration
  - Database containers for isolated testing

#### 3. Advanced Testing Patterns

- [ ] **Retry mechanisms for flaky tests**
  - `pytest-rerunfailures` integration
  - Smart retry strategies
  - Flaky test detection and reporting

- [ ] **Test data factories (`factory_boy`)**
  - Dynamic test data generation
  - Relationship management
  - Database persistence factories

- [ ] **API contract testing**
  - Pact consumer/provider tests
  - OpenAPI schema validation
  - Breaking change detection

- [ ] **Performance testing**
  - Locust integration for load testing
  - k6 for performance benchmarks
  - API response time assertions

- [ ] **Database fixtures & transactions**
  - Transaction rollback after tests
  - Database state snapshots
  - Multi-database test data setup

#### 4. Observability & Monitoring

- [ ] **Structured logging**
  - JSON logging format
  - Log levels configuration
  - Contextual logging (correlation IDs)

- [ ] **Test metrics & analytics**
  - Test execution time tracking
  - Failure rate monitoring
  - Test stability metrics

- [ ] **Enhanced debugging**
  - Screenshot on failure (already in web tests)
  - Video recording for failed tests
  - Browser console logs capture
  - Network traffic logging

#### 5. Advanced Code Organization

- [ ] **Pytest conftest.py hierarchy**
  - Nested conftest files for scope separation
  - Shared fixtures organization
  - Plugin discovery optimization

- [ ] **Custom markers & hooks**
  - Business-domain markers (`@smoke`, `@regression`, `@critical`)
  - Custom test selection strategies
  - Pre/post test execution hooks

- [ ] **Environment-specific configs**
  - Multi-environment support (dev/stage/prod)
  - Config inheritance and overrides
  - Environment validation

- [ ] **Secrets management**
  - `.env` files for local development (already used)
  - HashiCorp Vault integration for CI/CD
  - AWS Secrets Manager / Azure Key Vault
  - Encrypted secrets in repositories

### 📚 Recommended Learning Path

1. **Week 1-2**: Pytest advanced features (fixtures, parametrization, plugins)
2. **Week 3-4**: CI/CD setup (GitHub Actions, parallel execution, Allure)
3. **Week 5-6**: Test data management (factory_boy, database fixtures)
4. **Week 7-8**: API contract testing & performance testing
5. **Week 9-10**: Observability (structured logging, metrics, monitoring)

### 🎓 Key Senior-Level Skills to Demonstrate

- **Architecture decisions**: Why builder over factory? When to use fixtures vs helpers?
- **Performance optimization**: Test execution time, parallel strategies
- **Maintainability**: Easy onboarding, clear documentation, self-explanatory code
- **Production readiness**: CI/CD, monitoring, alerting, rollback strategies
- **Team collaboration**: Code reviews, knowledge sharing, mentoring
