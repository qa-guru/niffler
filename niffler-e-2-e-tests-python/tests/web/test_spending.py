import pytest
import allure
import logging
from datetime import date

from pages import MainPage
from models.spending import Spending
from models.category import Category
from models.user import User
from utils.constants import SuccessMessage
from utils.data_generators import random_amount, random_sentence
from helpers.auth_helper import setup_browser_with_api_login

logger = logging.getLogger(__name__)


@pytest.mark.web
#@pytest.mark.debug
@allure.id("500011")
@allure.title("WEB: Пользователь имеет возможность добавить трату")
@allure.tag("WEB")
@pytest.mark.usefixtures("setup_browser")
def test_should_add_new_spending(registered_user: User):
    """
    Test: User can add a new spending

    Migrated from Java test:
    niffler-e-2-e-tests/src/test/java/guru/qa/niffler/test/web/SpendingTest.java
    AllureId: 500011

    Steps:
    1. Register user and login via API
    2. Generate test data (category, amount, date, description)
    3. Navigate to spending form via header
    4. Fill in spending details
    5. Save spending
    6. Verify success alert appears
    7. Verify spending appears in the table
    """
    logger.info(f"═══════════════════════════════════════════════════════════════")
    logger.info(f"TEST START: Adding spending for user '{registered_user.username}'")
    logger.info(f"═══════════════════════════════════════════════════════════════")

    setup_browser_with_api_login(registered_user, use_ui_fallback=True)
    user = registered_user

    # Test data
    category = "Friends"
    amount = random_amount()
    current_date = date.today()
    description = random_sentence(3)

    logger.info(f"Test data generated: category='{category}', amount={amount}, date={current_date}, description='{description}'")

    # Create spending object for verification
    spending_to_check = Spending(
        category=Category(name=category),
        amount=amount,
        description=description,
        spend_date=current_date,
        currency=user.currency
    )

    # Navigate and add spending
    (MainPage()
        .header
        .add_spending_page()
        .set_new_spending_category(category)
        .set_new_spending_amount(amount)
        .select_spending_date_in_calendar(current_date)
        .set_new_spending_description(description)
        .save_spending()
        .check_alert_message(SuccessMessage.SPENDING_ADDED.content))

    # Verify spending in table
    MainPage().spending_table.check_table_contains(spending_to_check)
