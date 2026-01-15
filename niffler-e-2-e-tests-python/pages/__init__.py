"""
Page Object Model (POM) for Niffler application

This package contains page objects representing application pages.
Import pages directly from this module for convenience.

Example:
    from pages import LoginPage, MainPage

    LoginPage().fill_login_page("user", "pass").submit(MainPage())
"""

from pages.base_page import BasePage
from pages.login_page import LoginPage
from pages.main_page import MainPage
from pages.edit_spending_page import EditSpendingPage

__all__ = [
    'BasePage',
    'LoginPage',
    'MainPage',
    'EditSpendingPage',
]
