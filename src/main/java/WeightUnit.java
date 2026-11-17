
import java.math.BigDecimal;
import java.math.MathContext;

public final class QuantityWeight {

    private final double value;
    private final WeightUnit unit;

    // Constructor
    public QuantityWeight(double value, WeightUnit unit) {

        // Validate measurement
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }

        // Validate unit
        if (unit == null) {
            throw new IllegalArgumentException(
                    "Weight unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    // Return measurement value
    public double getValue() {
        return value;
    }

    // Return measurement unit
    public WeightUnit getUnit() {
        return unit;
    }

    // UC9: Compare weight measurements
    @Override
    public boolean equals(Object obj) {

        // Same object reference
        if (this == obj) {
            return true;
        }

        // Reject null or different class
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Cast Object reference to QuantityWeight
        QuantityWeight other = (QuantityWeight) obj;

        // Normalize both weights to kilograms
        return this.unit.normalizedKilograms(this.value)
                .compareTo(
                        other.unit.normalizedKilograms(other.value)
                ) == 0;
    }

    // Hash code must be consistent with equals()
    @Override
    public int hashCode() {
        return unit.normalizedKilograms(value).hashCode();
    }

    // UC9: Convert a numerical value between weight units
    public static double convert(
            double value,
            WeightUnit sourceUnit,
            WeightUnit targetUnit) {

        // Validate numerical input
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }

        // Validate units
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Weight units cannot be null");
        }

        // Same unit - no conversion needed
        if (sourceUnit == targetUnit) {
            return value;
        }

        // Convert source weight into kilograms
        double kilograms =
                sourceUnit.convertToBaseUnit(value);

        // Convert kilograms into target unit
        double result =
                targetUnit.convertFromBaseUnit(kilograms);

        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Converted weight is outside double range");
        }

        return result;
    }

    // UC9: Convert this weight to another unit
    public QuantityWeight convertTo(WeightUnit targetUnit) {

        double convertedValue = convert(
                this.value,
                this.unit,
                targetUnit
        );

        return new QuantityWeight(convertedValue, targetUnit);
    }

    // UC9: Add another weight
    // Result in first object's unit
    public QuantityWeight add(QuantityWeight other) {

        if (other == null) {
            throw new IllegalArgumentException(
                    "Second weight cannot be null");
        }

        return add(this, other, this.unit);
    }

    // UC9: Add with a specified target unit
    public QuantityWeight add(
            QuantityWeight other,
            WeightUnit targetUnit) {

        return add(this, other, targetUnit);
    }

    // Static addition method
    public static QuantityWeight add(
            QuantityWeight first,
            QuantityWeight second,
            WeightUnit targetUnit) {

        // Validate operands and target unit
        if (first == null || second == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Weights and target unit cannot be null");
        }

        // Convert first weight to kilograms
        BigDecimal firstKg = BigDecimal.valueOf(first.value)
                .multiply(
                        first.unit.getConversionFactor()
                );

        // Convert second weight to kilograms
        BigDecimal secondKg = BigDecimal.valueOf(second.value)
                .multiply(
                        second.unit.getConversionFactor()
                );

        // Add in kilograms
        BigDecimal totalKg = firstKg.add(secondKg);

        // Convert total to requested target unit
        BigDecimal convertedResult = totalKg.divide(
                targetUnit.getConversionFactor(),
                MathContext.DECIMAL64
        );

        double result = convertedResult.doubleValue();

        // Validate result
        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Addition result is outside double range");
        }

        // Return new object
        return new QuantityWeight(result, targetUnit);
    }

    // Overloaded addition using raw values
    public static QuantityWeight add(
            double value1,
            WeightUnit unit1,
            double value2,
            WeightUnit unit2,
            WeightUnit targetUnit) {

        QuantityWeight first =
                new QuantityWeight(value1, unit1);

        QuantityWeight second =
                new QuantityWeight(value2, unit2);

        return add(first, second, targetUnit);
    }

    // Display measurement
    @Override
    public String toString() {
        return value + " " + unit;
    }
}
