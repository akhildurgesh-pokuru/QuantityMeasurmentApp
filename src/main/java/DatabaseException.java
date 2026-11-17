
// UC16: Represents database-specific failures.
//
// Extending RuntimeException avoids forcing
// every controller and service to catch SQLException.
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(
            String message,
            Throwable cause) {

        // Keep the original exception for debugging.
        super(message, cause);
    }
}
