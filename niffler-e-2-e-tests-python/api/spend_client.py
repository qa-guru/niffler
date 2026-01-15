"""API client for Niffler Spend service"""

from typing import List, Optional
import allure

from api.base_client import BaseAPIClient
from models.spending import Spending
from utils.config import CFG


class SpendClient(BaseAPIClient):
    """Client for interacting with niffler-spend service"""

    def __init__(self):
        """Initialize Spend API client"""
        super().__init__(base_url=CFG.spend_url)

    @allure.step("Create spending via API")
    def create_spending(self, spending: Spending, username: str) -> Spending:
        """
        Create new spending via API (internal endpoint)

        Args:
            spending: Spending object to create
            username: Username for authentication

        Returns:
            New Spending instance with ID from server
        """
        # Serialize spending to API format
        payload = spending.to_dict()
        payload["username"] = username
        payload["category"]["username"] = username

        # Make API request
        response = self.post('/internal/spends/add', json=payload)
        response.raise_for_status()

        # Deserialize response to new Spending object
        return Spending.from_dict(response.json())

    @allure.step("Get user spendings via API")
    def get_spendings(self, token: str, from_date: Optional[str] = None,
                     to_date: Optional[str] = None) -> List[Spending]:
        """
        Get user spendings via API

        Args:
            token: Authorization token
            from_date: Filter from date (optional)
            to_date: Filter to date (optional)

        Returns:
            List of spendings
        """
        headers = {'Authorization': f'Bearer {token}'}
        params = {}
        if from_date:
            params['from'] = from_date
        if to_date:
            params['to'] = to_date

        response = self.get('/api/spends', headers=headers, params=params)
        response.raise_for_status()

        return [Spending.from_dict(s) for s in response.json()]

    @allure.step("Delete spendings via API")
    def delete_spendings(self, username: str, spending_ids: List[str]):
        """
        Delete spendings by IDs via API (internal endpoint)

        Args:
            username: Username
            spending_ids: List of spending IDs to delete
        """
        response = self.delete(
            '/internal/spends/remove',
            params={
                "username": username,
                "ids": ",".join(spending_ids)
            }
        )
        response.raise_for_status()
