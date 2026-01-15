"""SearchField component for search functionality in Niffler application"""

from selene import browser, be, have, command
from selenium.webdriver.common.keys import Keys
import allure

from pages.components.base_component import BaseComponent


class SearchField(BaseComponent):
    """SearchField component matching Java SearchField class"""

    def __init__(self, root=None):
        """
        Initialize search field component

        Args:
            root: Root element of search field. If None, uses default selector
        """
        if root is None:
            root = browser.element('input[aria-label="search"]')
        super().__init__(root)

        self._clear_search_input_btn = browser.element('#input-clear')

    @allure.step("Search for: {query}")
    def search(self, query: str) -> 'SearchField':
        """
        Perform search with given query

        Args:
            query: Search query string

        Returns:
            self for method chaining
        """
        self.clear_if_not_empty()
        self.self.type(query).press_enter()
        return self

    @allure.step("Clear search field if not empty")
    def clear_if_not_empty(self) -> 'SearchField':
        """
        Clear search field if it contains text

        Returns:
            self for method chaining
        """
        # Check if field is not empty
        if self.self.get(be.present).get_attribute('value'):
            self._clear_search_input_btn.click()
            self.self.should(have.value(''))
        return self
