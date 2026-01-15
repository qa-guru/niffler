import pytest
import allure
from datetime import date

from api.spend_client import SpendClient
from builders.spending_builder import SpendingBuilder
from models.user import User
from utils.data_generators import random_amount, random_sentence


@pytest.mark.api
@allure.id("600001")
@allure.title("API: Add and delete spending via API")
@allure.tag("API")
def test_should_add_and_delete_spending_via_api(generated_user: User, spending_cleanup):
    """
    Test: Create and delete spending via API only

    Steps:
    1. Register user via API
    2. Create spending via API
    3. Verify spending was created
    4. Cleanup happens automatically via fixture
    """
    from helpers.user_helper import register_user

    # Register user
    user = register_user(generated_user.username, generated_user.test_data.password)

    # Create spending object using builder pattern
    spending = (SpendingBuilder()
        .with_category("Friends")
        .with_amount(random_amount())
        .with_description(random_sentence(3))
        .with_date(date.today())
        .with_currency(user.currency)
        .build()
    )

    # Create spending via API
    spend_client = SpendClient()
    created_spending = spend_client.create_spending(spending, user.username)

    # Track spending for automatic cleanup
    spending_cleanup.track(str(created_spending.id), user.username)

    # Verify spending was created
    assert created_spending.id is not None, "Spending ID should be set after creation"
    assert created_spending.username == user.username, "Username should match"

    # Cleanup happens automatically after test via fixture (even on assertion failure)
