
import java.io.Serializable;
import java.time.LocalDateTime;

// UC15: Stores an operation record.
//
// The class is immutable:
// all fields are private and final.
public final class QuantityMeasurementEntity
        implements Serializable {

    private static final long serialVersionUID = 1L;

    // Operation name: ADD, SUBTRACT, CONVERT, etc.
    private final String operation;

    // The first input measurement.
    private final QuantityDTO first;

    // Optional second input.
    private final QuantityDTO second;

    // Quantity result for conversion/add/subtract.
    private final QuantityDTO result;

    // Comparison result: true or false.
    private final Boolean comparisonResult;

    // Division result: dimensionless ratio.
    private final Double scalarResult;

    // Error message, if something failed.
    private final String errorMessage;

    // Time of the operation.
    private final String timestamp;

    // Private constructor used by factory methods.
    private QuantityMeasurementEntity(
            String operation,
            QuantityDTO first,
            QuantityDTO second,
            QuantityDTO result,
            Boolean comparisonResult,
            Double scalarResult,
            String errorMessage) {

        this.operation = operation;
        this.first = first;
        this.second = second;
        this.result = result;
        this.comparisonResult = comparisonResult;
        this.scalarResult = scalarResult;
        this.errorMessage = errorMessage;

        this.timestamp = LocalDateTime.now().toString();
    }

    // Successful operation returning a quantity.
    public static QuantityMeasurementEntity quantityResult(
            String operation,
            QuantityDTO first,
            QuantityDTO second,
            QuantityDTO result) {

        return new QuantityMeasurementEntity(
                operation, first, second,
                result, null, null, null);
    }

    // Successful equality comparison.
    public static QuantityMeasurementEntity comparison(
            QuantityDTO first,
            QuantityDTO second,
            boolean equal) {

        return new QuantityMeasurementEntity(
                "COMPARE", first, second,
                null, equal, null, null);
    }

    // Successful division returning a scalar.
    public static QuantityMeasurementEntity division(
            QuantityDTO first,
            QuantityDTO second,
            double ratio) {

        return new QuantityMeasurementEntity(
                "DIVIDE", first, second,
                null, null, ratio, null);
    }

    // Failed operation.
    public static QuantityMeasurementEntity error(
            String operation,
            QuantityDTO first,
            QuantityDTO second,
            String message) {

        return new QuantityMeasurementEntity(
                operation, first, second,
                null, null, null, message);
    }

    public String getOperation() {
        return operation;
    }

    public QuantityDTO getFirst() {
        return first;
    }

    public QuantityDTO getSecond() {
        return second;
    }

    public QuantityDTO getResult() {
        return result;
    }

    public Boolean getComparisonResult() {
        return comparisonResult;
    }

    public Double getScalarResult() {
        return scalarResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // An error exists when the message is non-null.
    public boolean hasError() {
        return errorMessage != null;
    }

    // Display the operation result.
    @Override
    public String toString() {

        if (hasError()) {
            return operation + " ERROR: " + errorMessage;
        }

        if (comparisonResult != null) {
            return "COMPARE result: " + comparisonResult;
        }

        if (scalarResult != null) {
            return "DIVIDE result: " + scalarResult;
        }

        return operation + " result: " + result;
    }
}
