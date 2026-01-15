from selene import browser, be, have
import allure

from pages.base_page import BasePage
from pages.components.header import Header
from pages.components.spending_table import SpendingTable
from utils.config import CFG


class MainPage(BasePage['MainPage']):
    """Main page matching Java MainPage class"""

    URL = f"{CFG.front_url}main"

    def __init__(self):
        super().__init__()
        self._header = Header()
        self._spending_table = SpendingTable()

    @property
    def header(self) -> Header:
        """Get header component"""
        return self._header

    @property
    def spending_table(self) -> SpendingTable:
        """Get spending table component with scroll into view"""
        browser.execute_script("arguments[0].scrollIntoView(true);", self._spending_table.self())
        return self._spending_table

    @allure.step("Check that Main Page is loaded")
    def wait_for_page_loaded(self) -> 'MainPage':
        """Wait for page to be fully loaded"""
        self._header.self.should(be.visible).should(have.text('Niffler'))
        self._spending_table.self.should(be.visible).should(have.text('History of Spendings'))
        self.logger.info("Main Page loaded successfully")
        return self

    @allure.step("Open Main Page")
    def open(self) -> 'MainPage':
        """Open main page"""
        self.logger.info(f"Opening Main Page: {self.URL}")
        browser.open(self.URL)
        return self.wait_for_page_loaded()
