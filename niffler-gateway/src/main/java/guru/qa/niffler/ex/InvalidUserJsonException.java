package guru.qa.niffler.ex;

public class InvalidUserJsonException extends RuntimeException {

    public InvalidUserJsonException() {
    }

    public InvalidUserJsonException(String message) {
        super(message);
    }

    public InvalidUserJsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserJsonException(Throwable cause) {
        super(cause);
    }

    public InvalidUserJsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
