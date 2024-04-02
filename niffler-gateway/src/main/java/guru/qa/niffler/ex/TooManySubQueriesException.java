package guru.qa.niffler.ex;

public class TooManySubQueriesException extends RuntimeException {
    public TooManySubQueriesException(String message) {
        super(message);
    }
}
