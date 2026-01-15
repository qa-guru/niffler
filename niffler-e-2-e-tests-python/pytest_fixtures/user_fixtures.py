"""User fixtures for authentication and user management"""

import pytest
from models.user import User
from helpers.user_helper import register_user


@pytest.fixture
def generated_user() -> User:
    """Generate random user (not registered)."""
    return User.generate()


@pytest.fixture
def registered_user(generated_user: User) -> User:
    """Register user via API."""
    return register_user(generated_user.username, generated_user.test_data.password)


# TODO: Add session-scoped fixtures if needed for performance optimization
# For now, all tests use isolated users (generated_user → registered_user)
