package guru.qa.niffler.ex;

public class InvalidCategoryNameException extends RuntimeException {
  public InvalidCategoryNameException(String message) {
    super(message);
  }
}
