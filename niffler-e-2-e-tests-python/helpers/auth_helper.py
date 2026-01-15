"""Authentication helper functions for browser and API"""

import hashlib
import base64
import secrets
import requests
import logging
from urllib.parse import urlparse, parse_qs
from selene import browser, have

from models.user import User
from utils.config import CFG

logger = logging.getLogger(__name__)


class OAuth2Helper:
    """OAuth2 PKCE flow helper"""

    @staticmethod
    def generate_code_verifier() -> str:
        """Generate PKCE code verifier"""
        return base64.urlsafe_b64encode(secrets.token_bytes(32)).decode('utf-8').rstrip('=')

    @staticmethod
    def generate_code_challenge(code_verifier: str) -> str:
        """Generate PKCE code challenge from verifier"""
        digest = hashlib.sha256(code_verifier.encode('utf-8')).digest()
        return base64.urlsafe_b64encode(digest).decode('utf-8').rstrip('=')


def setup_browser_with_ui_login(user: User):
    """UI login fallback when API login fails"""
    from pages import LoginPage, MainPage

    logger.info(f"UI login for user: {user.username}")
    browser.open(CFG.front_url)

    login_page = LoginPage().wait_for_page_loaded()
    main_page = login_page.fill_login_page(user.username, user.test_data.password).submit(MainPage())

    logger.info(f"UI login successful: {browser.driver.current_url}")
    return main_page


def _inject_cookie(name: str, value: str):
    """Inject cookie on current domain"""
    browser.driver.add_cookie({'name': name, 'value': value})


def _inject_tokens_to_browser(tokens: dict):
    """Inject JSESSIONID cookie and id_token to browser on both domains"""
    # Open frontend → redirects to auth
    browser.open(CFG.front_url)
    browser.should(have.url_containing(CFG.auth_url))

    # Inject JSESSIONID on auth domain
    _inject_cookie('JSESSIONID', tokens['jsessionid'])

    # Navigate to frontend and inject tokens there
    browser.open(CFG.front_url)
    _inject_cookie('JSESSIONID', tokens['jsessionid'])
    browser.execute_script("window.localStorage.setItem('id_token', arguments[0]);", tokens['id_token'])

    # Verify tokens
    if not browser.driver.get_cookie('JSESSIONID'):
        raise Exception("Failed to set JSESSIONID cookie")
    if not browser.execute_script("return window.localStorage.getItem('id_token');"):
        raise Exception("Failed to set id_token in localStorage")


def setup_browser_with_api_login(user: User, use_ui_fallback: bool = True):
    """
    API login with token injection (matches Java ApiLoginExtension).
    ~3x faster than UI login.
    """
    from pages import MainPage

    try:
        logger.info(f"API login for user: {user.username}")
        tokens = perform_api_login(user)

        _inject_tokens_to_browser(tokens)

        browser.open(f"{CFG.front_url}main")
        main_page = MainPage()
        main_page.wait_for_page_loaded()

        logger.info("API login successful")
        return main_page

    except Exception as e:
        logger.warning(f"API login failed: {e}")
        if use_ui_fallback:
            logger.info("Falling back to UI login")
            return setup_browser_with_ui_login(user)
        raise


def get_csrf_token(session: requests.Session, endpoint: str = "register", params: dict = None) -> str:
    """
    Get CSRF token from auth endpoint

    Args:
        session: Requests session
        endpoint: Endpoint path (e.g., 'register', 'oauth2/authorize')
        params: Optional query parameters

    Returns:
        str: CSRF token from XSRF-TOKEN cookie

    Raises:
        Exception: If CSRF token cannot be obtained
    """
    url = f"{CFG.auth_url}{endpoint}"
    response = session.get(url, params=params, allow_redirects=True)
    csrf_token = session.cookies.get('XSRF-TOKEN')

    if not csrf_token:
        raise Exception(f"Failed to obtain CSRF token from {endpoint}")

    return csrf_token


def _get_csrf_token(session: requests.Session, code_challenge: str) -> str:
    """Get CSRF token from authorization endpoint (internal wrapper)"""
    auth_params = {
        'response_type': 'code',
        'client_id': 'client',
        'scope': 'openid',
        'redirect_uri': f"{CFG.front_url}authorized",
        'code_challenge': code_challenge,
        'code_challenge_method': 'S256'
    }

    return get_csrf_token(session, endpoint="oauth2/authorize", params=auth_params)


def _extract_auth_code_from_redirects(session: requests.Session, login_response) -> str:
    """Follow redirects to extract authorization code"""
    redirect_count = 0
    max_redirects = 10

    while login_response.status_code in [301, 302, 303, 307, 308] and redirect_count < max_redirects:
        redirect_count += 1
        location = login_response.headers.get('Location')

        if 'code=' in location:
            return parse_qs(urlparse(location).query)['code'][0]

        # Build next URL
        if location.startswith('http'):
            next_url = location
        elif location.startswith('/'):
            next_url = f"{CFG.auth_url.rstrip('/')}{location}"
        else:
            next_url = f"{CFG.auth_url}{location}"

        login_response = session.get(next_url, allow_redirects=False)

    raise Exception(f"Failed to obtain authorization code after {redirect_count} redirects")


def _exchange_code_for_token(session: requests.Session, code: str, code_verifier: str) -> str:
    """Exchange authorization code for id_token"""
    response = session.post(
        f"{CFG.auth_url}oauth2/token",
        data={
            'client_id': 'client',
            'redirect_uri': f"{CFG.front_url}authorized",
            'grant_type': 'authorization_code',
            'code': code,
            'code_verifier': code_verifier
        }
    )

    if response.status_code != 200:
        raise Exception(f"Token exchange failed: {response.status_code}")

    id_token = response.json().get('id_token')
    if not id_token:
        raise Exception("Failed to obtain id_token")

    return id_token


def perform_api_login(user: User) -> dict:
    """
    OAuth2 PKCE login via API (matches Java ApiLoginExtension).

    Returns:
        dict: {'id_token': str, 'jsessionid': str, 'xsrf_token': str}
    """
    session = requests.Session()

    # Generate PKCE parameters
    code_verifier = OAuth2Helper.generate_code_verifier()
    code_challenge = OAuth2Helper.generate_code_challenge(code_verifier)

    # Get CSRF token
    csrf_token = _get_csrf_token(session, code_challenge)

    # Login and get authorization code
    login_response = session.post(
        f"{CFG.auth_url}login",
        data={'username': user.username, 'password': user.test_data.password, '_csrf': csrf_token},
        allow_redirects=False
    )

    if login_response.status_code == 200:
        raise Exception("Login failed - wrong credentials or CSRF token")

    code = _extract_auth_code_from_redirects(session, login_response)
    jsessionid = session.cookies.get('JSESSIONID')

    # Exchange code for token
    id_token = _exchange_code_for_token(session, code, code_verifier)

    return {'id_token': id_token, 'jsessionid': jsessionid, 'xsrf_token': csrf_token}
