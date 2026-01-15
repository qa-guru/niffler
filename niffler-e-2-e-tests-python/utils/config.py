import os
from typing import Optional
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()


class Config:
    """Configuration singleton matching Java Config class"""
    _instance: Optional['Config'] = None

    def __init__(self):
        # Frontend URL
        self._front_url = os.getenv('FRONT_URL', 'http://localhost:3000/')

        # Gateway URL
        self._gateway_url = os.getenv('GATEWAY_URL', 'http://localhost:8090/')

        # Auth URL
        self._auth_url = os.getenv('AUTH_URL', 'http://localhost:9000/')

        # Spend service URL
        self._spend_url = os.getenv('SPEND_URL', 'http://localhost:8093/')

        # Database settings
        self._db_host = os.getenv('DB_HOST', 'localhost')
        self._db_port = int(os.getenv('DB_PORT', '5432'))
        self._db_user = os.getenv('DB_USER', 'postgres')
        self._db_password = os.getenv('DB_PASSWORD', 'secret')

        # Browser settings
        self._browser = os.getenv('BROWSER', 'chrome')
        self._headless = os.getenv('HEADLESS', 'false').lower() == 'true'

    @classmethod
    def get_instance(cls) -> 'Config':
        """Get singleton instance"""
        if cls._instance is None:
            cls._instance = Config()
        return cls._instance

    @property
    def front_url(self) -> str:
        """Get frontend URL"""
        return self._front_url

    @property
    def gateway_url(self) -> str:
        """Get gateway URL"""
        return self._gateway_url

    @property
    def auth_url(self) -> str:
        """Get auth URL"""
        return self._auth_url

    @property
    def spend_url(self) -> str:
        """Get spend service URL"""
        return self._spend_url

    @property
    def db_host(self) -> str:
        return self._db_host

    @property
    def db_port(self) -> int:
        return self._db_port

    @property
    def db_user(self) -> str:
        return self._db_user

    @property
    def db_password(self) -> str:
        return self._db_password

    @property
    def browser(self) -> str:
        return self._browser

    @property
    def headless(self) -> bool:
        return self._headless


# Singleton instance for easy import
CFG = Config.get_instance()
