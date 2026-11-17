
// UC15: Custom exception for measurement operations.
public class QuantityMeasurementException
        extends RuntimeException {

    // Create an exception with a custom message.
    public QuantityMeasurementException(String message) {
        super(message);
    }

    // Preserve the original exception as the cause.
    public QuantityMeasurementException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}
