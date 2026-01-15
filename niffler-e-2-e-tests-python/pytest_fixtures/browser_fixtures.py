"""Browser setup fixtures for web tests"""

import pytest
from selene import browser
from utils.config import CFG


@pytest.fixture(scope='function')
def setup_browser(request):
    """Setup browser before each test (only for @pytest.mark.web tests)"""
    # Skip browser setup for non-web tests
    if 'web' not in request.keywords:
        yield
        return

    # Configure browser based on config
    browser.config.driver_name = CFG.browser
    browser.config.window_width = 1920
    browser.config.window_height = 1080
    browser.config.timeout = 10

    # Set headless mode if configured
    if CFG.headless:
        from selenium import webdriver
        options = webdriver.ChromeOptions()
        options.add_argument('--headless')
        options.add_argument('--no-sandbox')
        options.add_argument('--disable-dev-shm-usage')
        browser.config.driver_options = options

    yield

    # Keep browser open only if test has @pytest.mark.debug
    if 'debug' in request.keywords:
        print("\n[DEBUG] Browser will stay open for manual inspection (test marked with @pytest.mark.debug)")
        print("[DEBUG] Press Ctrl+C to close browser and exit...")
        try:
            import time
            while True:
                time.sleep(1)
        except KeyboardInterrupt:
            print("\n[DEBUG] Closing browser...")
            browser.quit()
    else:
        browser.quit()
