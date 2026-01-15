from abc import ABC, abstractmethod
from selene import browser, be, have
import allure
import logging
from typing import TypeVar, Generic

from utils.config import CFG

T = TypeVar('T', bound='BasePage')


class BasePage(ABC, Generic[T]):
    """Base page class matching Java BasePage"""

    def __init__(self):
        self.logger = logging.getLogger(self.__class__.__name__)
        self._alert = browser.element('.MuiSnackbar-root')
        self._form_errors = browser.all('.input__helper-text')

    @abstractmethod
    def wait_for_page_loaded(self) -> T:
        """Wait for page to be loaded - must be implemented by subclasses"""
        pass

    @allure.step("Check that alert message appears: {expected_text}")
    def check_alert_message(self, expected_text: str) -> T:
        """Check success/error alert message"""
        self._alert.should(be.visible).should(have.text(expected_text))
        self.logger.info(f"✓ Alert message verified: '{expected_text}'")
        return self

    @allure.step("Check that form error message appears: {expected_texts}")
    def check_form_error_message(self, *expected_texts: str) -> T:
        """Check form validation error messages"""
        for text in expected_texts:
            self._form_errors.should(have.texts(*expected_texts))
        return self
