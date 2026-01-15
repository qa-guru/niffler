# Niffler E2E Tests - Python

Python/Selene implementation of Niffler end-to-end tests, migrated from Java/Selenide.

## Quick Start

```bash
# 1. Create and activate virtual environment
python3.13 -m venv .venv
source .venv/bin/activate  # On Windows: .venv\Scripts\activate

# 2. Install dependencies
pip install -r requirements.txt

# 3. Start Niffler services (Docker)
cd ..
bash docker-compose-dev.sh

# 4. Run tests
cd niffler-e-2-e-tests-python
pytest
```

## Project Structure

```
niffler-e-2-e-tests-python/
├── api/                        # API clients for backend services
│   ├── __init__.py
│   ├── base_client.py         # Base HTTP client with common methods
│   └── spend_client.py        # Client for niffler-spend service
│
├── builders/                   # Builder pattern for test data creation
│   ├── __init__.py
│   └── spending_builder.py    # Fluent API for creating Spending objects
│
├── db/                         # Database access layer
│   ├── __init__.py
│   ├── connection.py          # Connection pool management
│   └── repositories/
│       ├── __init__.py
│       └── spending_repository.py  # CRUD operations for spendings
│
├── helpers/                    # Helper functions (NOT fixtures)
│   ├── __init__.py
│   ├── user_helper.py         # User registration functions
│   └── auth_helper.py         # OAuth2 PKCE + API/UI login + token injection
│
├── models/                     # Data models (dataclasses)
│   ├── __init__.py
│   ├── user.py                # User model
│   ├── spending.py            # Spending model
│   └── category.py            # Category model
│
├── pages/                      # Page Object Model
│   ├── __init__.py
│   ├── base_page.py           # Base page class
│   ├── main_page.py           # Main page
│   ├── edit_spending_page.py  # Edit spending page
│   └── components/            # Reusable page components
│       ├── __init__.py
│       ├── base_component.py  # Base component class
│       ├── header.py          # Header component
│       └── spending_table.py  # Spending table component
│
├── pytest_fixtures/            # Pytest fixture functions
│   ├── __init__.py
│   ├── browser_fixtures.py    # Browser setup and configuration
│   ├── user_fixtures.py       # User authentication and login fixtures
│   └── cleanup_fixtures.py    # Test data cleanup with yield pattern
│
├── fixtures/                   # Test data files (images, PDFs, CSVs, etc.)
│   ├── images/                # Test images
│   ├── documents/             # Test documents
│   └── data/                  # Test data files
│
├── tests/                      # Test files organized by type
│   ├── __init__.py
│   ├── web/                   # Web UI tests
│   │   ├── __init__.py
│   │   └── test_spending.py
│   ├── api/                   # API tests
│   │   └── __init__.py
│   └── graphql/               # GraphQL tests
│       └── __init__.py
│
├── utils/                      # Utility modules
│   ├── __init__.py
│   ├── config.py              # Configuration management
│   ├── constants.py           # Constants and enums
│   └── data_generators.py    # Test data generators
│
├── .env                        # Environment variables
├── .gitignore                  # Git ignore rules
├── conftest.py                 # Pytest configuration (registers fixture plugins)
├── pytest.ini                  # Pytest settings
├── requirements.txt            # Python dependencies
└── README.md                   # This file
```

## Architecture & Design Patterns

### 1. Page Object Model (POM)

Page objects encapsulate page structure and behavior:

```python
class MainPage(BasePage):
    @property
    def spending_table(self) -> SpendingTable:
        return self._spending_table

    def open(self) -> 'MainPage':
        browser.open(self.URL)
        return self
```

### 2. Component Pattern

Reusable UI components:

```python
class Header(BaseComponent):
    def add_spending_page(self):
        self._add_spending_button.click()
        return EditSpendingPage()
```

### 3. Builder Pattern

Fluent API for creating test data:

```python
spending = (SpendingBuilder()
    .with_category("Food")
    .with_random_amount()
    .with_description("Lunch")
    .build())
```

### 4. Repository Pattern

Data access abstraction:

```python
class SpendingRepository:
    @staticmethod
    def get_user_spendings(username: str) -> List[Spending]:
        # DB query implementation
```

### 5. API Client Pattern

Service clients for API interactions:

```python
class SpendClient(BaseAPIClient):
    def create_spending(self, spending: Spending, token: str):
        # API call implementation
```

### 6. Fixtures vs Helpers

**Important distinction:**

- **Fixtures** (`pytest_fixtures/`): Pytest fixtures with `@pytest.fixture` decorator - auto-injected into tests
- **Helpers** (`helpers/`): Regular functions that you call explicitly

```python
# Fixture (in pytest_fixtures/user_fixtures.py)
@pytest.fixture
def api_login_user(generated_user: User) -> User:
    user = register_user(...)  # Calls helper
    setup_browser_with_api_login(user)  # Calls helper
    return user

# Helper (in helpers/user_helper.py)
def register_user(username: str, password: str) -> User:
    # Registration logic

# Helper (in helpers/auth_helper.py)
def setup_browser_with_api_login(user: User):
    # OAuth2 API login + token injection
```

## Configuration

Configuration is managed through:

1. **Environment variables** (`.env` file)
2. **Config class** (`utils/config.py`)
3. **pytest.ini** for test runner settings

### Environment Variables

```env
# Niffler URLs for Docker deployment
FRONT_URL=http://frontend.niffler.dc/
GATEWAY_URL=http://gateway.niffler.dc/
AUTH_URL=http://auth.niffler.dc:9000/

# Browser settings
BROWSER=chrome
HEADLESS=false
KEEP_BROWSER_OPEN=true

# Database settings (optional)
DB_HOST=localhost
DB_PORT=5432
DB_USER=postgres
DB_PASSWORD=secret
```

## Running Tests

### Test Markers

Tests are categorized using pytest markers:

- `@pytest.mark.web` - Web UI tests
- `@pytest.mark.api` - API tests
- `@pytest.mark.graphql` - GraphQL tests
- `@pytest.mark.smoke` - Smoke tests
- `@pytest.mark.regression` - Regression tests

### Run Commands

```bash
# Run all tests
pytest

# Run specific test type
pytest -m web
pytest -m api

# Run specific test file
pytest tests/web/test_spending.py

# Run with specific marker
pytest -m "web and smoke"

# Run in headless mode
HEADLESS=true pytest

# Run in parallel
pytest -n 4

# Generate Allure report
pytest --alluredir=allure-results
allure serve allure-results
```

## Test Example

```python
@pytest.mark.web
@allure.id("500011")
@allure.title("WEB: Пользователь имеет возможность добавить трату")
def test_should_add_new_spending(api_login_user: User):
    """User can add a new spending"""
    user = api_login_user
    category = "Friends"
    amount = random_amount()
    description = random_sentence(3)

    spending = Spending(
        category=Category(name=category),
        amount=amount,
        description=description,
        spend_date=date.today(),
        currency=user.currency
    )

    (MainPage()
        .header
        .add_spending_page()
        .set_new_spending_category(category)
        .set_new_spending_amount(amount)
        .select_spending_date_in_calendar(date.today())
        .set_new_spending_description(description)
        .save_spending()
        .check_alert_message(SuccessMessage.SPENDING_ADDED.content))

    MainPage().spending_table.check_table_contains(spending)
```

## Best Practices

1. **Use Page Objects** - Never access selectors directly in tests
2. **Use Builders** - Create test data with builders, not constructors
3. **Use Allure Steps** - Add `@allure.step()` to important methods
4. **Type Hints** - Always use type hints for better IDE support
5. **Docstrings** - Document classes and public methods
6. **Single Responsibility** - Each class should have one responsibility
7. **DRY** - Don't repeat yourself, extract common logic
8. **Test Independence** - Tests should not depend on each other
9. **Clean Up** - Clean test data after tests (DB, API)
10. **Fixtures vs Helpers** - Use fixtures for pytest dependency injection, helpers for explicit calls

## Fixtures

Available pytest fixtures (defined in `pytest_fixtures/`):

### User Fixtures

#### `generated_user` - Generate random user
Generates a random user without registration or login.

```python
def test_something(generated_user: User):
    user = generated_user  # User object (NOT registered)
```

#### `api_login_user` - User with API login ⭐ **RECOMMENDED**
Generates user, registers via API, performs OAuth2 login, injects tokens into browser.

**This is MUCH faster than UI login!**

```python
@pytest.mark.web
@pytest.mark.usefixtures("setup_browser")
def test_something(api_login_user: User):
    user = api_login_user  # Browser already logged in!
    MainPage().add_spending(...)
```

**How it works:**
1. Generates random user
2. Registers via API (`POST /register`)
3. Performs OAuth2 PKCE flow via API:
   - `GET /oauth2/authorize`
   - `POST /login`
   - `POST /oauth2/token` → gets `id_token` + `JSESSIONID`
4. Opens browser and injects tokens:
   - `browser.add_cookie('JSESSIONID', jsessionid)`
   - `localStorage.setItem('id_token', id_token)`
5. Opens main page

**Performance:** ~2-3 seconds (vs ~5-8 seconds for UI login)

#### `api_login_with_spendings` - User with pre-created spendings
Same as `api_login_user` but creates spendings via API before login (TODO: implement spending creation).

```python
def test_edit_spending(api_login_with_spendings: User):
    user = api_login_with_spendings  # User already has spendings
```

#### `session_login_user` - Session-scoped user
Creates one user per test session (for performance optimization).

**⚠️ WARNING:** No test isolation! Use only for read-only tests.

```python
def test_readonly(session_login_user: User):
    user = session_login_user  # Same user for all tests
```

#### `reuse_session_login` - Reuse session across tests ⚡ **FASTEST**
Reuses browser session across multiple tests (login happens only once).

**⚠️ WARNING:** No test isolation! Tests share the same user and browser state.

```python
@pytest.mark.web
@pytest.mark.usefixtures("setup_browser")
def test_readonly(reuse_session_login: User):
    user = reuse_session_login  # Browser session reused!
```

**Performance:** ~2-3 seconds for first test, ~0.5 second for subsequent tests

**When to use:**
- Read-only tests that don't modify user data
- Performance testing
- Smoke tests

**When NOT to use:**
- Tests that add/edit/delete data
- Tests requiring clean state

### Browser Fixtures

#### `setup_browser` - Browser configuration
Auto-configures browser for web tests (auto-use for `@pytest.mark.web`).

```python
@pytest.mark.web
@pytest.mark.usefixtures("setup_browser")
def test_something(api_login_user: User):
    # Browser is already configured
```

### Cleanup Fixtures

#### `spending_cleanup` - Automatic cleanup
Automatic cleanup of created spendings after test (even on failure).

### Cleanup Fixture Usage

The `spending_cleanup` fixture uses **yield pattern** to guarantee cleanup even if test fails:

```python
@pytest.mark.api
def test_create_spending(generated_user: User, spending_cleanup):
    """Test with automatic cleanup"""
    user = register_user(generated_user.username, generated_user.test_data.password)

    # Create spending
    spending = SpendingBuilder().with_category("Food").build()
    created = spend_client.create_spending(spending, user.username)

    # Track for cleanup - CRITICAL: always track before assertions
    spending_cleanup.track(str(created.id), user.username)

    # Assertions
    assert created.id is not None

    # Cleanup happens automatically here (even on assertion failure)
```

**Benefits:**
- ✅ Guaranteed cleanup even if test fails
- ✅ No manual cleanup code in tests
- ✅ Batch deletion for efficiency (groups by username)
- ✅ Test isolation - each test cleans up after itself

## Key Differences from Java

| Feature | Java | Python |
|---------|------|--------|
| **Language** | Java 17+ | Python 3.13+ |
| **Browser Library** | Selenide | Selene |
| **Test Framework** | JUnit 5 | Pytest |
| **Assertions** | Selenide Conditions | Selene `be`/`have` |
| **Data Models** | Records | Dataclasses |
| **Page Objects** | Classes with generics | Classes with typing |
| **Fixtures** | JUnit Extensions | Pytest fixtures |
| **Allure** | `@Step` annotations | `@allure.step` decorator |

## IDE Setup

### VSCode
1. Open Command Palette: **Cmd+Shift+P** (macOS) or **Ctrl+Shift+P** (Windows/Linux)
2. Select **"Python: Select Interpreter"**
3. Choose `.venv/bin/python` from the list

### PyCharm
1. **Settings/Preferences** → **Project** → **Python Interpreter**
2. Click gear icon → **Add...**
3. Select **Existing environment** → specify path to `.venv/bin/python`

## Migration Status

### ✅ Completed
- Project structure with best practices (helpers vs fixtures separation)
- Data models (User, Spending, Category)
- Base page and component classes
- Page objects (MainPage, EditSpendingPage)
- Components (Header, SpendingTable)
- Utility classes (Config, Constants)
- Test: `test_should_add_new_spending` (AllureId: 500011)
- **OAuth2 PKCE API login with token injection** (matches Java `ApiLoginExtension`)
- **API login fixtures** (`api_login_user`, `reuse_session_login`)
- **Organized fixture structure** (`pytest_fixtures/` package)
- **Automatic cleanup fixtures** (yield pattern for guaranteed cleanup)

### 📋 TODO
- Implement API clients for all services
- Add GraphQL client and tests
- Implement database repositories
- Add performance testing support
- Add visual regression testing
- Implement parallel test execution
- Add CI/CD pipeline configuration

## Resources

- [Selene Documentation](https://yashaka.github.io/selene/)
- [Pytest Documentation](https://docs.pytest.org/)
- [Allure Framework](https://docs.qameta.io/allure/)
- [Java Tests Reference](../niffler-e-2-e-tests/)

## Notes

- `.venv` folder is in `.gitignore` and won't be committed
- All dependencies are installed only inside the virtual environment
- System remains clean
- Using Python 3.13 (stable) instead of Python 3.14 (preview)
- Keep browser open after test: set `KEEP_BROWSER_OPEN=true` in `.env` and press Ctrl+C when done
