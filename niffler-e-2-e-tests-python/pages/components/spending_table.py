from selene import browser, be, have
import allure
from typing import List

from pages.components.base_component import BaseComponent
from models.spending import Spending


class SpendingTable(BaseComponent):
    """Spending table component matching Java SpendingTable class"""

    def __init__(self):
        super().__init__(browser.element('#spendings'))
        self._period_menu = self._self.element('#period')
        self._currency_menu = self._self.element('#currency')
        self._menu_items = browser.all('.MuiList-padding li')
        self._delete_btn = self._self.element('#delete')
        self._popup = browser.element("div[role='dialog']")

        self._table_header = self._self.element('.MuiTableHead-root')
        self._header_cells = self._table_header.all('.MuiTableCell-root')
        self._table_rows = self._self.element('tbody').all('tr')
        self._search_field = self._self.element('input[type="text"]')

    @allure.step("Select table period: {period}")
    def select_period(self, period: str):
        """Select time period filter"""
        self._period_menu.click()
        self._menu_items.element_by(have.text(period)).click()
        return self

    @allure.step("Edit spending with description: {description}")
    def edit_spending(self, description: str):
        """Edit spending by description"""
        from pages.edit_spending_page import EditSpendingPage
        self._search_spending_by_description(description)
        row = self._table_rows.element_by(have.text(description))
        row.all('td')[5].click()
        return EditSpendingPage()

    @allure.step("Delete spending with description: {description}")
    def delete_spending(self, description: str):
        """Delete spending by description"""
        self._search_spending_by_description(description)
        row = self._table_rows.element_by(have.text(description))
        row.all('td')[0].click()
        self._delete_btn.click()
        self._popup.element(have.text('Delete')).click()
        return self

    @allure.step("Search spending with description: {description}")
    def _search_spending_by_description(self, description: str):
        """Search for spending by description"""
        self._search_field.type(description)
        return self

    @allure.step("Check that table contains spending data")
    def check_table_contains(self, *expected_spendings: Spending):
        """Verify table contains expected spendings"""
        for spending in expected_spendings:
            # Check each spending appears in the table
            self._table_rows.should(have.size_greater_than(0))

            # Find the row containing the spending description and verify all fields
            if spending.description:
                self.logger.info(f"Verifying spending in table: {spending.description}")
                row = self._table_rows.element_by(have.text(spending.description))
                row.should(be.visible)

                # Verify other fields in the same row
                if spending.category:
                    row.should(have.text(spending.category.name))
                if spending.amount:
                    row.should(have.text(str(spending.amount)))
                self.logger.info(f"✓ Spending found in table: {spending.description}")
        return self

    @allure.step("Check that table has size: {expected_size}")
    def check_table_size(self, expected_size: int):
        """Verify table row count"""
        self._table_rows.should(have.size(expected_size))
        return self
