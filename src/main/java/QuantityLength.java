
import java.math.BigDecimal;
import java.util.Objects;

public final class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    // Constructor
    public QuantityLength(double value, LengthUnit unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Measurement must be a finite number");
        }

        this.value = value;
        this.unit = Objects.requireNonNull(
                unit, "Unit cannot be null");
    }

    // Getters
    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    // UC3 / UC4 / UC8: Equality
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        QuantityLength other = (QuantityLength) obj;

        // Delegate normalization to LengthUnit
        BigDecimal first =
                this.unit.normalizedInches(this.value);

        BigDecimal second =
                other.unit.normalizedInches(other.value);

        return first.compareTo(second) == 0;
    }

    @Override
    public int hashCode() {
        return unit.normalizedInches(value).hashCode();
    }

    // UC5 / UC8: Convert between units
    public static double convert(
            double value,
            LengthUnit sourceUnit,
            LengthUnit targetUnit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Value must be a finite number");
        }

        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Units cannot be null");
        }

        if (sourceUnit == targetUnit) {
            return value;
        }

        // Ask source unit to convert into feet
        double baseValue =
                sourceUnit.convertToBaseUnit(value);

        // Ask target unit to convert from feet
        return targetUnit.convertFromBaseUnit(baseValue);
    }

    // UC5: Convert this measurement to another unit
    public QuantityLength convertTo(LengthUnit targetUnit) {

        double convertedValue = convert(
                this.value,
                this.unit,
                targetUnit
        );

        return new QuantityLength(
                convertedValue, targetUnit);
    }

    // UC6: Add, result in first object's unit
    public QuantityLength add(QuantityLength other) {

        if (other == null) {
            throw new IllegalArgumentException(
                    "Second quantity cannot be null");
        }

        double convertedValue = convert(
                other.value,
                other.unit,
                this.unit
        );

        double sum = this.value + convertedValue;

        if (!Double.isFinite(sum)) {
            throw new ArithmeticException(
                    "Addition result is outside double range");
        }

        return new QuantityLength(sum, this.unit);
    }

    // UC7: Add with explicit target unit
    public static QuantityLength add(
            QuantityLength first,
            QuantityLength second,
            LengthUnit targetUnit) {

        if (first == null || second == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Quantities and target unit cannot be null");
        }

        QuantityLength sum = first.add(second);

        return sum.convertTo(targetUnit);
    }

    // UC7: Instance method overload
    public QuantityLength add(
            QuantityLength other,
            LengthUnit targetUnit) {

        return QuantityLength.add(this, other, targetUnit);
    }

    // UC7: Static overload using raw values
    public static QuantityLength add(
            double value1, LengthUnit unit1,
            double value2, LengthUnit unit2,
            LengthUnit targetUnit) {

        QuantityLength first =
                new QuantityLength(value1, unit1);

        QuantityLength second =
                new QuantityLength(value2, unit2);

        return add(first, second, targetUnit);
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}
