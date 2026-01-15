from selene import browser, be, command
from selenium.webdriver.common.keys import Keys
import allure
from datetime import date, datetime

from pages.base_page import BasePage
from models.spending import Spending
from utils.config import CFG


class EditSpendingPage(BasePage['EditSpendingPage']):
    """Edit spending page matching Java EditSpendingPage class"""

    URL = f"{CFG.front_url}spending"

    def __init__(self):
        super().__init__()
        self._amount_input = browser.element('#amount')
        self._category_input = browser.element('#category')
        self._categories = browser.all('.MuiChip-root')
        self._description_input = browser.element('#description')
        self._date_input = browser.element('input[name="date"]')
        self._currency_select = browser.element('#currency')
        self._cancel_btn = browser.element('#cancel')
        self._save_btn = browser.element('#save')

    @allure.step("Check that Edit Spending Page is loaded")
    def wait_for_page_loaded(self) -> 'EditSpendingPage':
        """Wait for page to be fully loaded"""
        self._amount_input.should(be.visible)
        self.logger.info("Edit Spending Page loaded successfully")
        return self

    @allure.step("Fill spending data from object")
    def fill_page(self, spending: Spending) -> 'EditSpendingPage':
        """Fill all spending fields from Spending object"""
        self.select_spending_date_in_calendar(spending.spend_date)
        self.set_new_spending_amount(spending.amount)
        self.set_new_spending_currency(spending.currency)
        self.set_new_spending_category(spending.category.name)
        self.set_new_spending_description(spending.description)
        return self

    @allure.step("Select new spending currency: {currency}")
    def set_new_spending_currency(self, currency: str) -> 'EditSpendingPage':
        """Set spending currency"""
        self._currency_select.click()
        browser.all('li').element_by(be.text(currency)).click()
        return self

    @allure.step("Select new spending category: {category}")
    def set_new_spending_category(self, category: str) -> 'EditSpendingPage':
        """Set spending category"""
        self.logger.info(f"Setting category: {category}")
        self._category_input.perform(command.select_all)
        self._category_input.clear()
        self._category_input.type(category)
        return self

    @allure.step("Set new spending amount: {amount}")
    def set_new_spending_amount(self, amount: float) -> 'EditSpendingPage':
        """Set spending amount"""
        self.logger.info(f"Setting amount: {amount}")
        self._amount_input.perform(command.select_all)
        self._amount_input.clear()
        self._amount_input.type(str(amount))
        return self

    @allure.step("Select spending date in calendar")
    def select_spending_date_in_calendar(self, spending_date: date) -> 'EditSpendingPage':
        """Select date from calendar picker"""
        # For simplicity, we'll paste the date directly
        # A full calendar component implementation would go here
        date_str = spending_date.strftime('%m/%d/%Y')
        self.logger.info(f"Setting date: {date_str}")
        self.paste_spending_date(date_str)
        return self

    @allure.step("Select today's date")
    def select_date_today(self) -> 'EditSpendingPage':
        """Select today's date"""
        return self.select_spending_date_in_calendar(date.today())

    @allure.step("Paste spending date: {date_str}")
    def paste_spending_date(self, date_str: str) -> 'EditSpendingPage':
        """Paste date directly into date field"""
        self._date_input.perform(command.select_all)
        self._date_input.type(Keys.BACKSPACE)
        if date_str:
            self._date_input.type(date_str)
        return self

    @allure.step("Set new spending description: {description}")
    def set_new_spending_description(self, description: str) -> 'EditSpendingPage':
        """Set spending description"""
        self.logger.info(f"Setting description: {description}")
        self._description_input.perform(command.select_all)
        self._description_input.clear()
        self._description_input.type(description)
        return self

    @allure.step("Click submit button to create new spending")
    def save_spending(self) -> 'EditSpendingPage':
        """Save spending"""
        self.logger.info("Saving new spending")
        self._save_btn.click()
        return self

    @allure.step("Cancel spending creation")
    def cancel_spending(self):
        """Cancel and return to main page"""
        from pages import MainPage
        self._cancel_btn.click()
        return MainPage()
