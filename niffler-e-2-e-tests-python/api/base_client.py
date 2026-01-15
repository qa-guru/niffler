"""Base HTTP client for API interactions"""

import requests
import logging
from typing import Optional, Dict, Any
import allure

logger = logging.getLogger(__name__)


class BaseAPIClient:
    """Base API client with common HTTP methods"""

    def __init__(self, base_url: str, timeout: int = 10):
        """
        Initialize API client

        Args:
            base_url: Base URL for API endpoints
            timeout: Request timeout in seconds
        """
        self.base_url = base_url.rstrip('/')
        self.timeout = timeout
        self.session = requests.Session()

    @allure.step("GET {endpoint}")
    def get(self, endpoint: str, **kwargs) -> requests.Response:
        """
        Perform GET request

        Args:
            endpoint: API endpoint path
            **kwargs: Additional arguments for requests.get

        Returns:
            Response object
        """
        url = f"{self.base_url}{endpoint}"
        self._log_request("GET", url, **kwargs)

        response = self.session.get(url, timeout=self.timeout, **kwargs)

        self._log_response(response)
        self._attach_response(response)
        return response

    @allure.step("POST {endpoint}")
    def post(self, endpoint: str, **kwargs) -> requests.Response:
        """
        Perform POST request

        Args:
            endpoint: API endpoint path
            **kwargs: Additional arguments for requests.post

        Returns:
            Response object
        """
        url = f"{self.base_url}{endpoint}"
        self._log_request("POST", url, **kwargs)

        response = self.session.post(url, timeout=self.timeout, **kwargs)

        self._log_response(response)
        self._attach_response(response)
        return response

    @allure.step("PUT {endpoint}")
    def put(self, endpoint: str, **kwargs) -> requests.Response:
        """
        Perform PUT request

        Args:
            endpoint: API endpoint path
            **kwargs: Additional arguments for requests.put

        Returns:
            Response object
        """
        url = f"{self.base_url}{endpoint}"
        self._log_request("PUT", url, **kwargs)

        response = self.session.put(url, timeout=self.timeout, **kwargs)

        self._log_response(response)
        self._attach_response(response)
        return response

    @allure.step("DELETE {endpoint}")
    def delete(self, endpoint: str, **kwargs) -> requests.Response:
        """
        Perform DELETE request

        Args:
            endpoint: API endpoint path
            **kwargs: Additional arguments for requests.delete

        Returns:
            Response object
        """
        url = f"{self.base_url}{endpoint}"
        self._log_request("DELETE", url, **kwargs)

        response = self.session.delete(url, timeout=self.timeout, **kwargs)

        self._log_response(response)
        self._attach_response(response)
        return response

    def _log_request(self, method: str, url: str, **kwargs):
        """
        Log HTTP request details

        Args:
            method: HTTP method (GET, POST, PUT, DELETE)
            url: Full request URL
            **kwargs: Request parameters (json, params, headers, etc.)
        """
        logger.info(f"{method} {url}")

        if kwargs.get('json'):
            logger.debug(f"Request payload: {kwargs['json']}")
        if kwargs.get('params'):
            logger.debug(f"Request params: {kwargs['params']}")
        if kwargs.get('headers'):
            logger.debug(f"Request headers: {kwargs['headers']}")

    def _log_response(self, response: requests.Response):
        """
        Log HTTP response details

        Args:
            response: Response object
        """
        logger.info(f"Response status: {response.status_code}")
        logger.debug(f"Response body: {response.text}")

    def _attach_response(self, response: requests.Response):
        """
        Attach response to Allure report

        Args:
            response: Response object
        """
        try:
            allure.attach(
                response.text,
                name=f"Response {response.status_code}",
                attachment_type=allure.attachment_type.JSON if 'json' in response.headers.get('content-type', '') else allure.attachment_type.TEXT
            )
        except Exception:
            pass  # Ignore attachment errors
