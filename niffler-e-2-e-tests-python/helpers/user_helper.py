"""User registration and management helper functions"""

import requests
import time

from models.user import User, TestData
from utils.config import CFG
from helpers.auth_helper import get_csrf_token


def register_user(username: str, password: str) -> User:
    """
    Register new user via API
    Matches RestCreateUserExtension.createUser() from Java

    Args:
        username: Username for registration
        password: Password for registration

    Returns:
        User object with credentials
    """
    session = requests.Session()

    # Step 1: GET CSRF token for registration
    csrf_token = get_csrf_token(session, endpoint="register")

    # Step 2: POST /register with credentials
    register_response = session.post(
        f"{CFG.auth_url}register",
        data={
            'username': username,
            'password': password,
            'passwordSubmit': password,
            '_csrf': csrf_token
        },
        allow_redirects=False
    )

    if register_response.status_code not in [200, 201, 302]:
        raise Exception(f"User registration failed: {register_response.status_code}, Response: {register_response.text}")

    # Wait for Kafka consumer to process user creation (max 10 seconds)
    time.sleep(2)

    # Create user object
    user = User(
        username=username,
        test_data=TestData(password=password)
    )

    return user
