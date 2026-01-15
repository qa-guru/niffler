"""Builder pattern for creating Spending test data"""

from datetime import date
from typing import Optional

from models.spending import Spending
from models.category import Category
from utils.data_generators import random_amount, random_sentence


class SpendingBuilder:
    """
    Builder for creating Spending objects with fluent API

    Example:
        spending = (SpendingBuilder()
            .with_category("Food")
            .with_random_amount()
            .with_description("Lunch")
            .build())
    """

    def __init__(self):
        """Initialize builder with default values"""
        self._category: Optional[Category] = None
        self._amount: Optional[float] = None
        self._description: Optional[str] = None
        self._spend_date: date = date.today()
        self._currency: str = "RUB"

    def with_category(self, category: str) -> 'SpendingBuilder':
        """
        Set spending category

        Args:
            category: Category name

        Returns:
            Builder instance for chaining
        """
        self._category = Category(name=category)
        return self

    def with_amount(self, amount: float) -> 'SpendingBuilder':
        """
        Set spending amount

        Args:
            amount: Spending amount

        Returns:
            Builder instance for chaining
        """
        self._amount = amount
        return self

    def with_random_amount(self, min_val: int = 100, max_val: int = 10000) -> 'SpendingBuilder':
        """
        Set random spending amount

        Args:
            min_val: Minimum amount
            max_val: Maximum amount

        Returns:
            Builder instance for chaining
        """
        self._amount = random_amount(min_val, max_val)
        return self

    def with_description(self, description: str) -> 'SpendingBuilder':
        """
        Set spending description

        Args:
            description: Description text

        Returns:
            Builder instance for chaining
        """
        self._description = description
        return self

    def with_random_description(self, words: int = 3) -> 'SpendingBuilder':
        """
        Set random spending description

        Args:
            words: Number of words in description

        Returns:
            Builder instance for chaining
        """
        self._description = random_sentence(words)
        return self

    def with_date(self, spend_date: date) -> 'SpendingBuilder':
        """
        Set spending date

        Args:
            spend_date: Date of spending

        Returns:
            Builder instance for chaining
        """
        self._spend_date = spend_date
        return self

    def with_currency(self, currency: str) -> 'SpendingBuilder':
        """
        Set spending currency

        Args:
            currency: Currency code (RUB, USD, EUR, etc.)

        Returns:
            Builder instance for chaining
        """
        self._currency = currency
        return self

    def build(self) -> Spending:
        """
        Build Spending object with configured values

        Returns:
            Spending instance
        """
        return Spending(
            category=self._category or Category(name="Friends"),
            amount=self._amount if self._amount is not None else random_amount(),
            description=self._description or random_sentence(3),
            spend_date=self._spend_date,
            currency=self._currency
        )
