"""Repository for Spending database operations"""

from typing import List, Optional
from datetime import date

from db.connection import DB
from models.spending import Spending
from models.category import Category


class SpendingRepository:
    """Repository for spending database operations"""

    @staticmethod
    def get_user_spendings(username: str) -> List[Spending]:
        """
        Get all spendings for user from database

        Args:
            username: Username to get spendings for

        Returns:
            List of Spending objects
        """
        with DB.get_connection('niffler-spend') as conn:
            with conn.cursor() as cursor:
                cursor.execute("""
                    SELECT id, username, spend_date, currency, category, amount, description
                    FROM spend
                    WHERE username = %s
                    ORDER BY spend_date DESC
                """, (username,))

                spendings = []
                for row in cursor.fetchall():
                    spending = Spending(
                        id=row[0],
                        username=row[1],
                        spend_date=row[2],
                        currency=row[3],
                        category=Category(name=row[4]),
                        amount=float(row[5]),
                        description=row[6]
                    )
                    spendings.append(spending)

                return spendings

    @staticmethod
    def delete_spending(spending_id: int):
        """
        Delete spending by ID

        Args:
            spending_id: ID of spending to delete
        """
        with DB.get_connection('niffler-spend') as conn:
            with conn.cursor() as cursor:
                cursor.execute("DELETE FROM spend WHERE id = %s", (spending_id,))

    @staticmethod
    def delete_all_user_spendings(username: str):
        """
        Delete all spendings for user

        Args:
            username: Username to delete spendings for
        """
        with DB.get_connection('niffler-spend') as conn:
            with conn.cursor() as cursor:
                cursor.execute("DELETE FROM spend WHERE username = %s", (username,))

    @staticmethod
    def create_spending(spending: Spending) -> int:
        """
        Create new spending in database

        Args:
            spending: Spending object to create

        Returns:
            ID of created spending
        """
        with DB.get_connection('niffler-spend') as conn:
            with conn.cursor() as cursor:
                cursor.execute("""
                    INSERT INTO spend (username, spend_date, currency, category, amount, description)
                    VALUES (%s, %s, %s, %s, %s, %s)
                    RETURNING id
                """, (
                    spending.username,
                    spending.spend_date,
                    spending.currency,
                    spending.category.name if spending.category else None,
                    spending.amount,
                    spending.description
                ))

                return cursor.fetchone()[0]
