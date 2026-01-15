"""Calendar component for date selection in Niffler application"""

from selene import browser, be, have
import allure
from datetime import date
import time
from calendar import month_name

from pages.components.base_component import BaseComponent


class Calendar(BaseComponent):
    """Calendar component matching Java Calendar class"""

    DATE_REGEXP = r"^\w+ \d{4}$"

    def __init__(self, root=None):
        """
        Initialize calendar component

        Args:
            root: Root element of calendar. If None, uses default selector
        """
        if root is None:
            root = browser.element('.MuiPickersLayout-root')
        super().__init__(root)

        self._input = browser.element('input[name="date"]')
        self._calendar_button = browser.element('button[aria-label*="Choose date"]')
        self._prev_month_button = self.self.element('button[title="Previous month"]')
        self._next_month_button = self.self.element('button[title="Next month"]')
        self._current_month_and_year = self.self.element('.MuiPickersCalendarHeader-label')
        self._date_rows = self.self.all('.MuiDayCalendar-weekContainer')

    @allure.step("Select date in calendar: {date_to_select}")
    def select_date_in_calendar(self, date_to_select: date) -> 'Calendar':
        """
        Select a date in the calendar picker

        Args:
            date_to_select: Python date object to select

        Returns:
            self for method chaining
        """
        self._calendar_button.click()

        desired_year = date_to_select.year
        desired_month_index = date_to_select.month - 1  # 0-indexed like Java Calendar
        desired_day = date_to_select.day

        self._select_year(desired_year)
        self._select_month(desired_month_index)
        self._select_day(desired_day)

        return self

    def _select_year(self, expected_year: int):
        """Navigate to the correct year"""
        actual_year = self._get_actual_year()

        while actual_year > expected_year:
            self._prev_month_button.click()
            time.sleep(0.2)
            actual_year = self._get_actual_year()

        while actual_year < expected_year:
            self._next_month_button.click()
            time.sleep(0.2)
            actual_year = self._get_actual_year()

    def _select_month(self, desired_month_index: int):
        """Navigate to the correct month (0-indexed)"""
        actual_month = self._get_actual_month_index()

        while actual_month > desired_month_index:
            self._prev_month_button.click()
            time.sleep(0.2)
            actual_month = self._get_actual_month_index()

        while actual_month < desired_month_index:
            self._next_month_button.click()
            time.sleep(0.2)
            actual_month = self._get_actual_month_index()

    def _select_day(self, desired_day: int):
        """Click on the desired day in the calendar grid"""
        rows = self._date_rows

        for row in rows:
            days = row.all('button')
            for day_button in days:
                if day_button.get(be.visible).text == str(desired_day):
                    day_button.click()
                    return

    def _get_actual_month_index(self) -> int:
        """
        Get current month index from calendar header (0-indexed)

        Returns:
            Month index (0 = January, 11 = December)
        """
        self._current_month_and_year.should(have.text_matching(self.DATE_REGEXP))
        month_text = self._current_month_and_year.get(be.visible).text.split()[0]

        # Convert month name to index
        for i, name in enumerate(month_name):
            if name.upper() == month_text.upper():
                return i - 1  # month_name starts with empty string at index 0

        raise ValueError(f"Could not parse month from: {month_text}")

    def _get_actual_year(self) -> int:
        """
        Get current year from calendar header

        Returns:
            Year as integer
        """
        self._current_month_and_year.should(have.text_matching(self.DATE_REGEXP))
        year_text = self._current_month_and_year.get(be.visible).text.split()[1]
        return int(year_text)
