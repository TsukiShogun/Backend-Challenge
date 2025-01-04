package looqbox.challenge.microservicepokeapi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<String> handleApiRequestException(ApiRequestException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidSortingStrategyException.class)
    public ResponseEntity<String> handleInvalidSortingStrategyException(InvalidSortingStrategyException ex) {
        return ResponseEntity.badRequest().body("Invalid sorting strategy: " + ex.getMessage());
    }

    @ExceptionHandler(InvalidSortTypeException.class)
    public ResponseEntity<String> handleInvalidSortTypeException(InvalidSortTypeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(SortingException.class)
    public ResponseEntity<String> handleSortingException(SortingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while sorting the Pokémon list: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }

    @ExceptionHandler(NullKeyException.class)
    public ResponseEntity<String> handleNullKeyException(NullKeyException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(NullValueException.class)
    public ResponseEntity<String> handleNullValueException(NullValueException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(CacheCleanupException.class)
    public ResponseEntity<String> handleCacheCleanupException(CacheCleanupException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred during cache cleanup: " + ex.getMessage());
    }
}
