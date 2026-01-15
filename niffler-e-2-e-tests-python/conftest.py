"""
Pytest configuration file for Niffler E2E tests.

This file:
- Sets up Python path for imports
- Loads environment variables
- Registers pytest plugins (fixtures)
- Configures custom markers
"""

import sys
from pathlib import Path
from dotenv import load_dotenv

# Add project root to sys.path to allow imports from tests/ subdirectories
project_root = Path(__file__).parent
sys.path.insert(0, str(project_root))

# Load environment variables from .env file
load_dotenv()

# Register pytest plugins (all fixture modules)
pytest_plugins = [
    'pytest_fixtures.browser_fixtures',
    'pytest_fixtures.user_fixtures',
    'pytest_fixtures.cleanup_fixtures',
]


def pytest_configure(config):
    """Register custom pytest markers"""
    config.addinivalue_line(
        "markers", "web: mark test as web UI test"
    )
    config.addinivalue_line(
        "markers", "api: mark test as API test"
    )
    config.addinivalue_line(
        "markers", "debug: keep browser open after test for manual inspection"
    )
