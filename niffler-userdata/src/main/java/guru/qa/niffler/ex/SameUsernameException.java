package guru.qa.niffler.ex;

public class SameUsernameException extends RuntimeException {
    public SameUsernameException(String message) {
        super(message);
    }
}
