"""
Pytest fixture functions package (NOT test data files!)

For test data files (PNG, CSV, JSON), see /fixtures/ directory.

Modules:
- browser_fixtures.py: Browser setup and configuration
- user_fixtures.py: User authentication and login fixtures
- cleanup_fixtures.py: Test data cleanup with yield pattern

All fixtures are automatically registered via pytest_plugins in conftest.py.
No need to import them explicitly - they're available in all tests!

Available fixtures:
- setup_browser: Browser setup and teardown
- generated_user: Generate random user (not registered)
- registered_user: Register user via API
- spending_cleanup: Auto-cleanup spendings after test

Note: For login, explicitly call helpers.auth_helper.setup_browser_with_api_login() in tests
"""
