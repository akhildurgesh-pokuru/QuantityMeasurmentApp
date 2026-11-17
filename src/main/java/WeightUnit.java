
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public enum WeightUnit implements IMeasurable{

    KILOGRAM("1"),
    GRAM("0.001"),
    POUND("0.453592");

    private final BigDecimal conversionFactor;

    // Enum constructor
    WeightUnit(String factor) {
        this.conversionFactor = new BigDecimal(factor);
    }

    // Returns factor relative to kilograms
    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    // Convert this unit into kilograms
    public double convertToBaseUnit(double value) {

        validate(value);

        BigDecimal result = BigDecimal.valueOf(value)
                .multiply(conversionFactor);

        double converted = result.doubleValue();

        if (!Double.isFinite(converted)) {
            throw new ArithmeticException(
                    "Weight conversion overflow");
        }

        return converted;
    }

    // Convert kilograms into this unit
    public double convertFromBaseUnit(double baseValue) {

        validate(baseValue);

        BigDecimal result = BigDecimal.valueOf(baseValue)
                .divide(conversionFactor, MathContext.DECIMAL64);

        double converted = result.doubleValue();

        if (!Double.isFinite(converted)) {
            throw new ArithmeticException(
                    "Weight conversion overflow");
        }

        return converted;
    }

    // Common representation for equals and hashCode
    public BigDecimal normalizedKilograms(double value) {

        validate(value);

        return BigDecimal.valueOf(value)
                .multiply(conversionFactor)
                .setScale(5, RoundingMode.HALF_UP);
    }

    private static void validate(double value) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }
    }


    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public BigDecimal normalizedBaseValue(double value) {
        return normalizedKilograms(value);
    }

}
