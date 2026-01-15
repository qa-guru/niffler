"""Login page for Niffler application"""

from selene import browser, be
import allure

from pages.base_page import BasePage
from utils.config import CFG


class LoginPage(BasePage['LoginPage']):
    """Login page matching Java LoginPage class"""

    URL = f"{CFG.auth_url}login"

    def __init__(self):
        super().__init__()
        self._username_input = browser.element('input[name="username"]')
        self._password_input = browser.element('input[name="password"]')
        self._submit_button = browser.element('button[type="submit"]')
        self._register_button = browser.element('a[href="/register"]')
        self._error_container = browser.element('.form__error')

    @allure.step("Fill login page with credentials: username: {username}")
    def fill_login_page(self, username: str, password: str) -> 'LoginPage':
        """Fill login form with username and password"""
        self.set_username(username)
        self.set_password(password)
        return self

    @allure.step("Set username: {username}")
    def set_username(self, username: str) -> 'LoginPage':
        """Set username in login form"""
        self._username_input.type(username)
        return self

    @allure.step("Set password")
    def set_password(self, password: str) -> 'LoginPage':
        """Set password in login form"""
        self._password_input.type(password)
        return self

    @allure.step("Submit login form")
    def submit(self, expected_page=None):
        """
        Submit login form and return expected page

        Args:
            expected_page: Expected page instance after login (e.g., MainPage())
                          If None, returns self for error checking

        Returns:
            Expected page instance or self
        """
        self._submit_button.click()
        if expected_page:
            return expected_page.wait_for_page_loaded()
        return self

    @allure.step("Click register button")
    def do_register(self):
        """Navigate to registration page"""
        self._register_button.click()
        # TODO: Implement RegisterPage POM
        # Import here to avoid circular dependency
        # from pages.register_page import RegisterPage
        # return RegisterPage().wait_for_page_loaded()
        return self

    @allure.step("Check error on page: {error}")
    def check_error(self, error: str) -> 'LoginPage':
        """Check that error message is displayed"""
        self._error_container.should(be.visible).should(be.text_matching(error))
        return self

    @allure.step("Check that Login Page is loaded")
    def wait_for_page_loaded(self) -> 'LoginPage':
        """Wait for page to be fully loaded"""
        self._username_input.should(be.visible)
        self._password_input.should(be.visible)
        return self

    @allure.step("Open Login Page")
    def open(self) -> 'LoginPage':
        """Open login page"""
        browser.open(self.URL)
        return self.wait_for_page_loaded()
