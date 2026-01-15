import pytest
import os


# ========== Skip Examples ==========

@pytest.mark.skip(reason="WIP: Feature not ready")
def test_skip_work_in_progress():
    """Skip test that's not ready yet"""
    assert False


@pytest.mark.skipif(
    os.getenv("ENVIRONMENT") == "production",
    reason="Destructive test - skip on production"
)
def test_skip_on_production():
    """Dangerous test - don't run on prod"""
    # Delete test data, modify DB, etc.
    assert True


def test_skip_by_environment():
    """Skip based on environment variable"""
    env = os.getenv("ENVIRONMENT", "local")
    if env == "production":
        pytest.skip("Destructive operations not allowed on production")

    # Delete user data, cleanup DB, etc.
    assert True


@pytest.mark.xfail(reason="Known bug NIFF-123")
def test_known_bug():
    """Expected to fail until bug is fixed"""
    assert False


# ========== Environment Markers ==========

@pytest.mark.smoke
@pytest.mark.staging
@pytest.mark.production
def test_health_check():
    """Critical smoke test - safe for all envs"""
    # Check service is up, basic read-only validation
    assert True


@pytest.mark.smoke
@pytest.mark.staging
def test_user_registration_flow():
    """User registration - staging only (creates data)"""
    # Register new user, create test data
    assert True


@pytest.mark.regression
@pytest.mark.staging
def test_full_spending_workflow():
    """Full regression - staging only"""
    # Create user, add spending, edit, delete
    assert True


@pytest.mark.staging
@pytest.mark.api
def test_api_data_cleanup():
    """Cleanup test data via API - staging only"""
    # Delete test users, cleanup DB
    assert True


@pytest.mark.local
@pytest.mark.web
def test_debug_ui_elements():
    """Debug UI - local dev only"""
    # Inspect DOM, check CSS, test interactions
    assert True


@pytest.mark.production
@pytest.mark.smoke
def test_production_readonly():
    """Production smoke - read-only validation"""
    # Check API responds, DB is accessible (no writes!)
    assert True
