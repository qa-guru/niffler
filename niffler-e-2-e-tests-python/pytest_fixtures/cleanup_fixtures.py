"""Cleanup fixtures - yield pattern for guaranteed cleanup even on test failure"""

import pytest
from typing import List, Tuple, Dict


@pytest.fixture
def spending_cleanup():
    """Auto-cleanup spendings after test. Usage: spending_cleanup.track(id, username)"""
    from api.spend_client import SpendClient

    class SpendingCleanup:
        def __init__(self):
            self.spendings_to_delete: List[Tuple[str, str]] = []
            self.client = SpendClient()

        def track(self, spending_id: str, username: str):
            self.spendings_to_delete.append((username, spending_id))

        def track_multiple(self, spending_ids: List[str], username: str):
            for spending_id in spending_ids:
                self.track(spending_id, username)

        def cleanup(self):
            if not self.spendings_to_delete:
                return

            # Batch delete by username
            by_username: Dict[str, List[str]] = {}
            for username, spending_id in self.spendings_to_delete:
                by_username.setdefault(username, []).append(spending_id)

            for username, spending_ids in by_username.items():
                try:
                    self.client.delete_spendings(username, spending_ids)
                except Exception as e:
                    print(f"⚠ Cleanup failed for {username}: {e}")

    cleanup_helper = SpendingCleanup()
    yield cleanup_helper
    cleanup_helper.cleanup()
