package looqbox.challenge.microservicepokeapi.exception;

public class SortingException extends RuntimeException {
    public SortingException(String message) {
        super(message);
    }

    public SortingException(String message, Throwable cause) {
        super(message, cause);
    }
}
