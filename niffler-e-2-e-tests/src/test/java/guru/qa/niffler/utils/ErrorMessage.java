package guru.qa.niffler.utils;

public enum ErrorMessage {
  BAD_CREDENTIALS("Bad credentials"),
  CAN_NOT_ADD_CATEGORY("Can not add new category"),
  PASSWORDS_SHOULD_BE_EQUAL("Passwords should be equal"),
  CAN_NOT_PICK_FUTURE_DATE_FOR_SPENDING("You can not pick future date"),
  CAN_NOT_CREATE_SPENDING_WITHOUT_CATEGORY("Please choose category"),
  CAN_NOT_CREATE_SPENDING_WITHOUT_AMOUNT("Amount has to be not less then 0.01");


  public final String content;

  ErrorMessage(String content) {
    this.content = content;
  }
}
