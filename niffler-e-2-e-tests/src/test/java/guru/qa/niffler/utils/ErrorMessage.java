package guru.qa.niffler.utils;

public enum ErrorMessage {
  BAD_CREDENTIALS("Bad credentials"),
  CAN_NOT_ADD_CATEGORY("Can not add new category"),
  PASSWORDS_SHOULD_BE_EQUAL("Passwords should be equal"),
  USERNAME_SHOULD_NOT_CONTAINS_WHITESPACES("Username must not contain whitespace"),
  PASSWORD_SHOULD_NOT_CONTAINS_WHITESPACES("Password must not contain whitespace"),
  CAN_NOT_PICK_FUTURE_DATE_FOR_SPENDING("You can not pick future date"),
  CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY("Please choose category"),
  CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT("Amount has to be not less then 0.01"),
  CAN_NOT_CREATE_SPENDING_WITH_INCORRECT_DATE("Date must be between 01.01.1970 and today");

  public final String content;

  ErrorMessage(String content) {
    this.content = content;
  }
}
