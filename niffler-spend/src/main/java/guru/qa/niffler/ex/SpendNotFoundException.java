package guru.qa.niffler.ex;

public class SpendNotFoundException extends RuntimeException {
    public SpendNotFoundException(String message) {
        super(message);
    }
}
