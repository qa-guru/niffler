from dataclasses import dataclass
from datetime import date, datetime
from typing import Optional, Dict, Any
from uuid import UUID

from models.category import Category


@dataclass
class Spending:
    """Spending model matching SpendJson from Java"""
    category: Category
    amount: float
    description: str
    spend_date: Optional[date] = None
    currency: str = "RUB"
    id: Optional[UUID] = None
    username: Optional[str] = None

    def __post_init__(self):
        """Set default spend_date to today if not provided"""
        if self.spend_date is None:
            self.spend_date = date.today()

        # Convert Category name string to Category object if needed
        if isinstance(self.category, str):
            self.category = Category(name=self.category)

    def to_simple_string(self) -> str:
        """Format spending for display, matching Java's toSimpleString()"""
        currency_symbols = {
            'RUB': '₽',
            'USD': '$',
            'EUR': '€',
            'KZT': '₸'
        }
        symbol = currency_symbols.get(self.currency, self.currency)
        date_str = self.spend_date.strftime('%b %-d, %Y') if self.spend_date else ''
        return f"{self.category.name} | {self.amount} {symbol} | {self.description} | {date_str}"

    def to_dict(self) -> Dict[str, Any]:
        """
        Serialize Spending to API format

        Returns:
            Dictionary representation for API requests
        """
        result = {
            "category": self.category.to_dict(),
            "amount": self.amount,
            "currency": self.currency,
            "spendDate": self.spend_date.isoformat() if self.spend_date else date.today().isoformat(),
            "description": self.description
        }
        if self.id is not None:
            result["id"] = str(self.id)
        if self.username is not None:
            result["username"] = self.username
        return result

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'Spending':
        """
        Deserialize Spending from API response

        Args:
            data: Dictionary from API response

        Returns:
            Spending instance
        """
        # Parse spend_date from various possible formats
        spend_date = None
        if data.get("spendDate"):
            spend_date_str = data["spendDate"]
            try:
                # Try parsing ISO format first (YYYY-MM-DD)
                spend_date = datetime.fromisoformat(spend_date_str.split('T')[0]).date()
            except (ValueError, AttributeError):
                # Fallback to today if parsing fails
                spend_date = date.today()

        return cls(
            category=Category.from_dict(data["category"]) if isinstance(data.get("category"), dict) else Category(name=data.get("category", "")),
            amount=float(data.get("amount", 0.0)),
            description=data.get("description", ""),
            spend_date=spend_date or date.today(),
            currency=data.get("currency", "RUB"),
            id=UUID(data["id"]) if data.get("id") else None,
            username=data.get("username")
        )
