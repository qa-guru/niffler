from selene import browser, be
import allure

from pages.components.base_component import BaseComponent


class Header(BaseComponent):
    """Header component matching Java Header class"""

    def __init__(self):
        super().__init__(browser.element('#root header'))
        self._main_page_link = self._self.element("a[href*='/main']")
        self._add_spending_btn = self._self.element("a[href*='/spending']")
        self._menu_btn = self._self.element('button')
        self._menu = self._self.element("ul[role='menu']")
        self._menu_items = self._menu.all('li')

    @allure.step("Add new spending")
    def add_spending_page(self):
        """Navigate to add spending page"""
        from pages import EditSpendingPage
        self.logger.info("Navigating to Add Spending page")
        self._add_spending_btn.click()
        return EditSpendingPage()

    @allure.step("Open Friends page")
    def to_friends_page(self):
        """Navigate to friends page"""
        self._menu_btn.click()
        self._menu_items.element_by(be.text('Friends')).click()
        # Return FriendsPage when implemented
        pass

    @allure.step("Open All Peoples page")
    def to_all_peoples_page(self):
        """Navigate to all peoples page"""
        self._menu_btn.click()
        self._menu_items.element_by(be.text('All People')).click()
        # Return PeoplePage when implemented
        pass

    @allure.step("Open Profile page")
    def to_profile_page(self):
        """Navigate to profile page"""
        self._menu_btn.click()
        self._menu_items.element_by(be.text('Profile')).click()
        # Return ProfilePage when implemented
        pass

    @allure.step("Sign out")
    def sign_out(self):
        """Sign out from application"""
        self._menu_btn.click()
        self._menu_items.element_by(be.text('Sign out')).click()
        # Return LoginPage when implemented
        pass

    @allure.step("Go to main page")
    def to_main_page(self):
        """Navigate to main page"""
        from pages import MainPage
        self._main_page_link.click()
        return MainPage()
