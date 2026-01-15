"""
Reusable UI components for Niffler application

This package contains reusable components that can be used across multiple pages.
Import components directly from this module for convenience.

Example:
    from pages.components import Header, SpendingTable, Calendar

    header = Header()
    header.add_spending_page()
"""

from pages.components.base_component import BaseComponent
from pages.components.header import Header
from pages.components.spending_table import SpendingTable
from pages.components.calendar import Calendar
from pages.components.select_field import SelectField
from pages.components.search_field import SearchField

__all__ = [
    'BaseComponent',
    'Header',
    'SpendingTable',
    'Calendar',
    'SelectField',
    'SearchField',
]
