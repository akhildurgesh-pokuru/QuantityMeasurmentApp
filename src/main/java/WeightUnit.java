
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

<<<<<<< HEAD
public enum WeightUnit implements IMeasurable {
=======
public enum WeightUnit implements IMeasurable{
public enum WeightUnit {
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee

    KILOGRAM("1"),
    GRAM("0.001"),
    POUND("0.453592");

    private final BigDecimal conversionFactor;

    // Enum constructor
    WeightUnit(String factor) {
        this.conversionFactor = new BigDecimal(factor);
    }

    // Return the conversion factor relative to kilograms
    @Override
    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    // Convert this weight unit into kilograms
    @Override
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

    // Convert kilograms into this weight unit
    @Override
    public double convertFromBaseUnit(double baseValue) {

        validate(baseValue);

        BigDecimal result = BigDecimal.valueOf(baseValue)
                .divide(
                        conversionFactor,
                        MathContext.DECIMAL64
                );

        double converted = result.doubleValue();

        if (!Double.isFinite(converted)) {
            throw new ArithmeticException(
                    "Weight conversion overflow");
        }

        return converted;
    }

    // Normalize weights for equals() and hashCode()
    public BigDecimal normalizedKilograms(double value) {

        validate(value);

        return BigDecimal.valueOf(value)
                .multiply(conversionFactor)
                .setScale(5, RoundingMode.HALF_UP);
    }

    // Return unit name
    @Override
    public String getUnitName() {
        return name();
    }

    // Common method required by IMeasurable
    @Override
    public BigDecimal normalizedBaseValue(double value) {
        return normalizedKilograms(value);
    }

    // Validate input
    private static void validate(double value) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }
    }
}
