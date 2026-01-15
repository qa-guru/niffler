from enum import Enum


class SuccessMessage(Enum):
    """Success message constants matching Java SuccessMessage enum"""
    FRIEND_DELETED = "Friend is deleted"
    INVITATION_ACCEPTED = "Invitation is accepted"
    INVITATION_DECLINED = "Invitation is declined"
    SPENDING_ADDED = "New spending is successfully created"
    PROFILE_UPDATED = "Profile successfully updated"
    CATEGORY_ADDED = "You've added new category:"

    @property
    def content(self) -> str:
        return self.value


class ErrorMessage(Enum):
    """Error message constants matching Java ErrorMessage enum"""
    BAD_CREDENTIALS = "Bad credentials"
    CAN_NOT_ADD_CATEGORY = "Can not add new category"
    PASSWORDS_SHOULD_BE_EQUAL = "Passwords should be equal"
    USERNAME_SHOULD_NOT_CONTAINS_WHITESPACES = "Username must not contain whitespace"
    PASSWORD_SHOULD_NOT_CONTAINS_WHITESPACES = "Password must not contain whitespace"
    CAN_NOT_PICK_FUTURE_DATE_FOR_SPENDING = "You can not pick future date"
    CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY = "Please choose category"
    CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT = "Amount has to be not less then 0.01"
    CAN_NOT_CREATE_SPENDING_WITH_INCORRECT_DATE = "Date must be between 01.01.1970 and today"

    @property
    def content(self) -> str:
        return self.value


class CurrencyValues(Enum):
    """Currency values matching Java CurrencyValues enum"""
    RUB = ('₽', 'RUB')
    USD = ('$', 'USD')
    EUR = ('€', 'EUR')
    KZT = ('₸', 'KZT')

    def __init__(self, symbol: str, name: str):
        self.symbol = symbol
        self.currency_name = name
