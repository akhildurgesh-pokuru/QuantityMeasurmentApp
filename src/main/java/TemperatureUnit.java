
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.DoubleUnaryOperator;

// UC14: Temperature measurement units.
//
// Celsius is our common base unit.
// This enum supports conversions and equality,
// but does not support arithmetic on absolute temperatures.
public enum TemperatureUnit implements IMeasurable {

    // Celsius -> Celsius and Celsius -> Celsius
    // Both functions return the same value.
    CELSIUS(
            celsius -> celsius,
            celsius -> celsius
    ),

    // Fahrenheit -> Celsius:
    // (F - 32) * 5 / 9
    //
    // Celsius -> Fahrenheit:
    // C * 9 / 5 + 32
    FAHRENHEIT(
            fahrenheit -> (fahrenheit - 32.0) * 5.0 / 9.0,
            celsius -> celsius * 9.0 / 5.0 + 32.0
    ),

    // Kelvin -> Celsius:
    // K - 273.15
    //
    // Celsius -> Kelvin:
    // C + 273.15
    KELVIN(
            kelvin -> kelvin - 273.15,
            celsius -> celsius + 273.15
    );


    // ==========================================
    // Instance Variables
    // ==========================================

    // Converts a temperature into Celsius.
    private final DoubleUnaryOperator toCelsius;

    // Converts Celsius into this temperature unit.
    private final DoubleUnaryOperator fromCelsius;

    // Functional interface implemented using a lambda.
    // Temperature arithmetic is unsupported.
    private static final SupportsArithmetic ARITHMETIC_SUPPORT =
            () -> false;


    // ==========================================
    // Enum Constructor
    // ==========================================

    TemperatureUnit(
            DoubleUnaryOperator toCelsius,
            DoubleUnaryOperator fromCelsius) {

        // Store the conversion functions.
        this.toCelsius = toCelsius;
        this.fromCelsius = fromCelsius;
    }


    // ==========================================
    // Convert Temperature to Celsius
    // ==========================================

    @Override
    public double convertToBaseUnit(double value) {

        // Validate the input.
        validate(value);

        // Execute the lambda for the selected enum unit.
        //
        // Example:
        // FAHRENHEIT.convertToBaseUnit(32)
        //
        // (32 - 32) * 5 / 9 = 0 Celsius
        double result = toCelsius.applyAsDouble(value);

        // Reject non-finite conversion results.
        validateResult(result);

        return result;
    }


    // ==========================================
    // Convert Celsius to Target Unit
    // ==========================================

    @Override
    public double convertFromBaseUnit(double baseValue) {

        // baseValue is expressed in Celsius.
        validate(baseValue);

        // Execute the stored conversion lambda.
        //
        // Example:
        // FAHRENHEIT.convertFromBaseUnit(100)
        //
        // 100 * 9 / 5 + 32 = 212 Fahrenheit
        double result = fromCelsius.applyAsDouble(baseValue);

        validateResult(result);

        return result;
    }


    // ==========================================
    // Interface Methods
    // ==========================================

    @Override
    public String getUnitName() {

        return name();
    }

    @Override
    public BigDecimal getConversionFactor() {

        // Temperature cannot be converted accurately
        // using a single multiplication factor.
        //
        // Returning ONE is a compatibility placeholder
        // for the existing IMeasurable interface.
        //
        // Use convertToBaseUnit() and
        // convertFromBaseUnit() for actual conversions.
        return BigDecimal.ONE;
    }


    // ==========================================
    // Normalize Temperature for Equality
    // ==========================================

    @Override
    public BigDecimal normalizedBaseValue(double value) {

        // Convert to common Celsius representation.
        double celsius = convertToBaseUnit(value);

        // Round the common representation to six
        // decimal places for equals() and hashCode().
        //
        // Example:
        // 0 Celsius == 32 Fahrenheit == 273.15 Kelvin
        return BigDecimal.valueOf(celsius)
                .setScale(6, RoundingMode.HALF_UP);
    }


    // ==========================================
    // UC14: Arithmetic Capability
    // ==========================================

    @Override
    public boolean supportsArithmetic() {

        // Calls the lambda: () -> false
        return ARITHMETIC_SUPPORT.isSupported();
    }

    @Override
    public void validateOperationSupport(String operation) {

        // Reuse the interface's default validation.
        //
        // Because this enum returns false for arithmetic,
        // the default validation throws
        // UnsupportedOperationException.
        IMeasurable.super.validateOperationSupport(operation);
    }


    // ==========================================
    // Input Validation
    // ==========================================

    private static void validate(double value) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Temperature must be a finite number");
        }
    }

    private static void validateResult(double value) {

        if (!Double.isFinite(value)) {
            throw new ArithmeticException(
                    "Temperature conversion result is not finite");
        }
    }
}
