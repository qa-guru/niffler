from dataclasses import dataclass, field
from typing import Optional, List
from uuid import UUID
import random
import string

from models.category import Category
from models.spending import Spending


@dataclass
class TestData:
    """Test data container matching Java TestData record"""
    password: str
    categories: List[Category] = field(default_factory=list)
    spendings: List[Spending] = field(default_factory=list)
    friends: List['User'] = field(default_factory=list)
    outcome_invitations: List['User'] = field(default_factory=list)
    income_invitations: List['User'] = field(default_factory=list)


@dataclass
class User:
    """User model matching UserJson from Java"""
    username: str
    currency: str = "RUB"
    id: Optional[UUID] = None
    fullname: Optional[str] = None
    firstname: Optional[str] = None
    surname: Optional[str] = None
    photo: Optional[str] = None
    photo_small: Optional[str] = None
    friendship_status: Optional[str] = None
    test_data: Optional[TestData] = None

    @staticmethod
    def generate(username: Optional[str] = None, password: Optional[str] = None) -> 'User':
        """Generate a random user for testing"""
        if username is None:
            username = f"user_{''.join(random.choices(string.ascii_lowercase + string.digits, k=8))}"
        if password is None:
            password = "password123"

        test_data = TestData(password=password)
        return User(username=username, test_data=test_data)

    def add_test_data(self, test_data: TestData) -> 'User':
        """Add test data to user, matching Java's addTestData()"""
        return User(
            id=self.id,
            username=self.username,
            fullname=self.fullname,
            firstname=self.firstname,
            surname=self.surname,
            currency=self.currency,
            photo=self.photo,
            photo_small=self.photo_small,
            friendship_status=self.friendship_status,
            test_data=test_data
        )
