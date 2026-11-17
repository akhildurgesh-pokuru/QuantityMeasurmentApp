
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Generic immutable quantity supporting any unit category
 * that implements IMeasurable.
 *
 * @param <U> the unit type, such as LengthUnit or WeightUnit
 */
public final class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    // Constructor
    public Quantity(double value, U unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Measurement must be a finite number");
        }

        if (unit == null) {
            throw new IllegalArgumentException(
                    "Unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    // Getters
    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    // Compare two quantities
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

        // Safe to cast the Quantity object itself
        Quantity<?> other = (Quantity<?>) obj;

        // Reject different measurement categories
        if (this.unit.getClass() != other.unit.getClass()) {
            return false;
        }

        // Compare canonical measurement values
        BigDecimal first =
                this.unit.normalizedBaseValue(this.value);

        BigDecimal second =
                other.unit.normalizedBaseValue(other.value);

        return first.compareTo(second) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                unit.getClass(),
                unit.normalizedBaseValue(value)
        );
    }

    // Convert raw values between units of the same category
    public static <T extends IMeasurable> double convert(
            double value,
            T sourceUnit,
            T targetUnit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Measurement must be a finite number");
        }

        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Units cannot be null");
        }

        if (sourceUnit.getClass() != targetUnit.getClass()) {
            throw new IllegalArgumentException(
                    "Incompatible measurement categories");
        }

        if (sourceUnit == targetUnit) {
            return value;
        }

        double baseValue =
                sourceUnit.convertToBaseUnit(value);

        double result =
                targetUnit.convertFromBaseUnit(baseValue);

        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Conversion result is not finite");
        }

        return result;
    }

    // Convert an existing quantity to another unit
    public Quantity<U> convertTo(U targetUnit) {

        double convertedValue = convert(
                this.value,
                this.unit,
                targetUnit
        );

        return new Quantity<>(convertedValue, targetUnit);
    }

    // UC6: Add, result in first operand's unit
    public Quantity<U> add(Quantity<U> other) {

        return add(other, this.unit);
    }

    // UC7: Add, result in explicitly requested unit
    public Quantity<U> add(
            Quantity<U> other,
            U targetUnit) {

        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Other quantity and target unit cannot be null");
        }

        if (unit.getClass() != other.unit.getClass()
                || unit.getClass() != targetUnit.getClass()) {
            throw new IllegalArgumentException(
                    "Incompatible measurement categories");
        }

        // Convert both measurements into the target unit
        double firstValue = convert(
                this.value, this.unit, targetUnit
        );

        double secondValue = convert(
                other.value, other.unit, targetUnit
        );

        // Add
        double sum = firstValue + secondValue;

        if (!Double.isFinite(sum)) {
            throw new ArithmeticException(
                    "Addition result is not finite");
        }

        return new Quantity<>(sum, targetUnit);
    }

    // Static addition with an explicit target unit
    public static <T extends IMeasurable> Quantity<T> add(
            Quantity<T> first,
            Quantity<T> second,
            T targetUnit) {

        if (first == null || second == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Quantities and target unit cannot be null");
        }

        return first.add(second, targetUnit);
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}
