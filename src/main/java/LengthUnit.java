
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public enum LengthUnit implements IMeasurable{

    // Existing factors relative to inches
    FEET("12"),
    INCHES("1"),
    YARDS("36"),
    CENTIMETERS("0.39370078740157480315");

    private static final BigDecimal INCHES_PER_FOOT =
            BigDecimal.valueOf(12);

    private final BigDecimal conversionFactor;

    LengthUnit(String factor) {
        this.conversionFactor = new BigDecimal(factor);
    }

    // Keep existing method for UC3-UC7 compatibility
    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    // UC8: Convert this unit into feet (base unit)
    public double convertToBaseUnit(double value) {

        validate(value);

        BigDecimal inches = BigDecimal.valueOf(value)
                .multiply(conversionFactor);

        return inches.divide(
                INCHES_PER_FOOT,
                MathContext.DECIMAL64
        ).doubleValue();
    }

    // UC8: Convert feet (base unit) into this unit
    public double convertFromBaseUnit(double baseValue) {

        validate(baseValue);

        BigDecimal inches = BigDecimal.valueOf(baseValue)
                .multiply(INCHES_PER_FOOT);

        double result = inches.divide(
                conversionFactor,
                MathContext.DECIMAL64
        ).doubleValue();

        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Converted value is outside double range");
        }

        return result;
    }

    // Shared normalization for equality and hashing
    public BigDecimal normalizedInches(double value) {

        validate(value);

        return BigDecimal.valueOf(value)
                .multiply(conversionFactor)
                .setScale(6, RoundingMode.HALF_UP);
    }

    // Validate numerical values
    private static void validate(double value) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Measurement must be a finite number");
        }
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public BigDecimal normalizedBaseValue(double value) {
        return normalizedInches(value);
    }
}
