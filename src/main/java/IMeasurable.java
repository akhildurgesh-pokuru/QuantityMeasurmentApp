
import java.math.BigDecimal;

public interface IMeasurable {

    // ==========================================
    // Existing UC10 methods - DO NOT REMOVE
    // ==========================================

    // Return unit conversion factor.
    BigDecimal getConversionFactor();

    // Convert from current unit into its base unit.
    double convertToBaseUnit(double value);

    // Convert from base unit into current unit.
    double convertFromBaseUnit(double baseValue);

    // Return unit name.
    String getUnitName();

    // Return normalized value for equals and hashCode.
    BigDecimal normalizedBaseValue(double value);


    // ==========================================
    // UC14: Optional Arithmetic Support
    // ==========================================

    // By default, all existing unit categories
    // support arithmetic.
    //
    // Length, Weight and Volume inherit true.
    default boolean supportsArithmetic() {
        return true;
    }

    // These methods allow callers to check
    // individual operations.
    default boolean supportsAddition() {
        return supportsArithmetic();
    }

    default boolean supportsSubtraction() {
        return supportsArithmetic();
    }

    default boolean supportsDivision() {
        return supportsArithmetic();
    }

    // Check whether the selected operation is allowed.
    //
    // Existing categories don't need to override this.
    default void validateOperationSupport(String operation) {

        if (operation == null) {
            throw new IllegalArgumentException(
                    "Operation cannot be null");
        }

        boolean supported;

        switch (operation.toUpperCase()) {
            case "ADD":
            case "ADDITION":
                supported = supportsAddition();
                break;

            case "SUBTRACT":
            case "SUBTRACTION":
                supported = supportsSubtraction();
                break;

            case "DIVIDE":
            case "DIVISION":
                supported = supportsDivision();
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown arithmetic operation: " + operation);
        }

        // Throw an exception for unsupported operations.
        if (!supported) {
            throw new UnsupportedOperationException(
                    getUnitName()
                            + " does not support "
                            + operation.toLowerCase()
                            + " operation");
        }
    }
}
