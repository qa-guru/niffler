import random
from faker import Faker

fake = Faker()


def random_amount(min_amount: float = 0.01, max_amount: float = 10000.0) -> float:
    """Generate random amount matching Java randomAmount()"""
    amount = random.uniform(min_amount, max_amount)
    return round(amount, 2)


def random_sentence(word_count: int = 3) -> str:
    """Generate random sentence matching Java randomSentence()"""
    return fake.sentence(nb_words=word_count).rstrip('.')


def random_username() -> str:
    """Generate random username"""
    return fake.user_name()


def random_category_name() -> str:
    """Generate random category name"""
    categories = [
        "Food", "Transport", "Entertainment", "Shopping", "Health",
        "Education", "Bills", "Travel", "Sports", "Hobbies",
        "Friends", "Family", "Gifts", "Pets", "Other"
    ]
    return random.choice(categories)
