
import java.math.BigDecimal;
import java.math.MathContext;

public final class QuantityWeight {

    private final double value;
    private final WeightUnit unit;

    // Constructor
    public QuantityWeight(double value, WeightUnit unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }

        if (unit == null) {
            throw new IllegalArgumentException(
                    "Weight unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    // Getters
    public double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    // UC9: Equality of weights
    @Override
    public boolean equals(Object obj) {

        // Same reference
        if (this == obj) {
            return true;
        }

        // Reject null or another class
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        QuantityWeight other = (QuantityWeight) obj;

        // Convert both weights to normalized kilograms
        return this.unit.normalizedKilograms(this.value)
                .compareTo(
                        other.unit.normalizedKilograms(other.value)
                ) == 0;
    }

    @Override
    public int hashCode() {
        return unit.normalizedKilograms(value).hashCode();
    }

    // UC9: Convert raw values between weight units
    public static double convert(
            double value,
            WeightUnit sourceUnit,
            WeightUnit targetUnit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }

        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Weight units cannot be null");
        }

        if (sourceUnit == targetUnit) {
            return value;
        }

        // Source -> kilograms -> target
        double kilograms =
                sourceUnit.convertToBaseUnit(value);

        return targetUnit.convertFromBaseUnit(kilograms);
    }

    // UC9: Convert current weight to another unit
    public QuantityWeight convertTo(WeightUnit targetUnit) {

        double convertedValue = convert(
                this.value,
                this.unit,
                targetUnit
        );

        return new QuantityWeight(convertedValue, targetUnit);
    }

    // UC9: Add another weight
    // Result uses the first object's unit
    public QuantityWeight add(QuantityWeight other) {

        if (other == null) {
            throw new IllegalArgumentException(
                    "Second weight cannot be null");
        }

        return add(this, other, this.unit);
    }

    // UC9: Add with explicitly specified target unit
    public QuantityWeight add(
            QuantityWeight other,
            WeightUnit targetUnit) {

        return add(this, other, targetUnit);
    }

    // Static addition using two objects
    public static QuantityWeight add(
            QuantityWeight first,
            QuantityWeight second,
            WeightUnit targetUnit) {

        if (first == null || second == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Weights and target unit cannot be null");
        }

        // Convert both measurements to kilograms
        BigDecimal firstKg = BigDecimal.valueOf(first.value)
                .multiply(first.unit.getConversionFactor());

        BigDecimal secondKg = BigDecimal.valueOf(second.value)
                .multiply(second.unit.getConversionFactor());

        // Add in common base unit
        BigDecimal totalKg = firstKg.add(secondKg);

        // Convert total into requested unit
        BigDecimal result = totalKg.divide(
                targetUnit.getConversionFactor(),
                MathContext.DECIMAL64
        );

        double answer = result.doubleValue();

        if (!Double.isFinite(answer)) {
            throw new ArithmeticException(
                    "Addition result is outside double range");
        }

        return new QuantityWeight(answer, targetUnit);
    }

    // Addition using raw values
    public static QuantityWeight add(
            double value1, WeightUnit unit1,
            double value2, WeightUnit unit2,
            WeightUnit targetUnit) {

        QuantityWeight first =
                new QuantityWeight(value1, unit1);

        QuantityWeight second =
                new QuantityWeight(value2, unit2);

        return add(first, second, targetUnit);
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}
