from dataclasses import dataclass
from typing import Optional, Dict, Any
from uuid import UUID


@dataclass
class Category:
    """Category model matching CategoryJson from Java"""
    name: str
    id: Optional[UUID] = None
    username: Optional[str] = None
    archived: bool = False

    def __str__(self):
        return self.name

    def to_dict(self) -> Dict[str, Any]:
        """
        Serialize Category to API format

        Returns:
            Dictionary representation for API requests
        """
        result = {
            "name": self.name,
            "archived": self.archived
        }
        if self.id is not None:
            result["id"] = str(self.id)
        if self.username is not None:
            result["username"] = self.username
        return result

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'Category':
        """
        Deserialize Category from API response

        Args:
            data: Dictionary from API response

        Returns:
            Category instance
        """
        return cls(
            name=data.get("name", ""),
            id=UUID(data["id"]) if data.get("id") else None,
            username=data.get("username"),
            archived=data.get("archived", False)
        )
