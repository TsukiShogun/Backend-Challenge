package looqbox.challenge.microservicepokeapi.exception;

public class InvalidSortTypeException extends RuntimeException {
    public InvalidSortTypeException(String message) {
        super(message);
    }
}
