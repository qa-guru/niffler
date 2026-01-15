"""SelectField component for dropdown selections in Niffler application"""

from selene import browser, be, have
import allure

from pages.components.base_component import BaseComponent


class SelectField(BaseComponent):
    """SelectField component matching Java SelectField class"""

    def __init__(self, root):
        """
        Initialize select field component

        Args:
            root: Root element of the select field
        """
        super().__init__(root)
        self._input = self.self.element('input')

    @allure.step("Set value: {value}")
    def set_value(self, value: str) -> 'SelectField':
        """
        Set select field value by clicking and choosing option

        Args:
            value: Value text to select

        Returns:
            self for method chaining
        """
        self.self.click()
        browser.all('li[role="option"]').element_by(have.text(value)).click()
        return self

    @allure.step("Check that select value equals: {value}")
    def check_select_value_is_equal_to(self, value: str) -> 'SelectField':
        """
        Verify select field contains expected value

        Args:
            value: Expected value text

        Returns:
            self for method chaining
        """
        self.self.should(have.text(value))
        return self
