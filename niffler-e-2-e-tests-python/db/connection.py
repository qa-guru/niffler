"""Database connection management"""

import psycopg2
from psycopg2 import pool
from typing import Optional
from contextlib import contextmanager

from utils.config import CFG


class DatabaseConnection:
    """Database connection pool manager"""

    _instance: Optional['DatabaseConnection'] = None
    _connection_pool: Optional[pool.SimpleConnectionPool] = None

    def __init__(self):
        """Initialize database connection pool"""
        if DatabaseConnection._connection_pool is None:
            try:
                DatabaseConnection._connection_pool = psycopg2.pool.SimpleConnectionPool(
                    minconn=1,
                    maxconn=10,
                    host=CFG.db_host,
                    port=CFG.db_port,
                    user=CFG.db_user,
                    password=CFG.db_password,
                    database='niffler-spend'  # Default database
                )
            except Exception as e:
                raise Exception(f"Failed to create database connection pool: {e}")

    @classmethod
    def get_instance(cls) -> 'DatabaseConnection':
        """Get singleton instance"""
        if cls._instance is None:
            cls._instance = DatabaseConnection()
        return cls._instance

    @contextmanager
    def get_connection(self, database: str = 'niffler-spend'):
        """
        Get database connection from pool

        Args:
            database: Database name (niffler-spend, niffler-userdata, niffler-auth, niffler-currency)

        Yields:
            Database connection
        """
        conn = None
        try:
            # Get connection from pool
            conn = self._connection_pool.getconn()

            # Switch database if needed
            if conn.info.dbname != database:
                conn.close()
                conn = psycopg2.connect(
                    host=CFG.db_host,
                    port=CFG.db_port,
                    user=CFG.db_user,
                    password=CFG.db_password,
                    database=database
                )

            yield conn
            conn.commit()

        except Exception as e:
            if conn:
                conn.rollback()
            raise e

        finally:
            if conn:
                self._connection_pool.putconn(conn)

    def close_all_connections(self):
        """Close all connections in pool"""
        if self._connection_pool:
            self._connection_pool.closeall()


# Singleton instance
DB = DatabaseConnection.get_instance()
