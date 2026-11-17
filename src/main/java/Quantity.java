
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


    // ==============================================
    // UC12: Subtraction with implicit target unit
    // ==============================================

    /**
     * Subtract another quantity from this quantity.
     *
     * The result uses the first object's unit.
     *
     * Example:
     * 10 FEET - 6 INCHES = 9.5 FEET
     */
    public Quantity<U> subtract(Quantity<U> other) {

        // Check whether the second quantity is null.
        if (other == null) {
            throw new IllegalArgumentException(
                    "Second quantity cannot be null");
        }

        // Reuse the overloaded subtraction method.
        // this.unit means the first object's unit.
        return subtract(other, this.unit);
    }


    // ==============================================
    // UC12: Subtraction with explicit target unit
    // ==============================================

    /**
     * Subtract another quantity and return the result
     * in the specified target unit.
     *
     * Example:
     * 10 FEET - 6 INCHES = 114 INCHES
     */
    public Quantity<U> subtract(
            Quantity<U> other,
            U targetUnit) {

        // Step 1: Validate the second object.
        if (other == null) {
            throw new IllegalArgumentException(
                    "Second quantity cannot be null");
        }

        // Step 2: Validate the target unit.
        if (targetUnit == null) {
            throw new IllegalArgumentException(
                    "Target unit cannot be null");
        }

        // Step 3: Make sure both quantities and
        // the target unit belong to the same category.
        //
        // For example, FEET and INCHES are compatible.
        // FEET and KILOGRAM are not compatible.
        if (this.unit.getClass() != other.unit.getClass()
                || this.unit.getClass() != targetUnit.getClass()) {

            throw new IllegalArgumentException(
                    "Cannot subtract different measurement categories");
        }

        // Step 4: Convert the first measurement
        // into its category's common base unit.
        //
        // For length, this is FEET.
        // For weight, this is KILOGRAM.
        // For volume, this is LITRE.
        double firstBase =
                this.unit.convertToBaseUnit(this.value);

        // Step 5: Convert the second measurement
        // into the same common base unit.
        double secondBase =
                other.unit.convertToBaseUnit(other.value);

        // Step 6: Subtract the converted values.
        double difference = firstBase - secondBase;

        // Step 7: Reject overflow or invalid results.
        if (!Double.isFinite(difference)) {
            throw new ArithmeticException(
                    "Subtraction result is not finite");
        }

        // Step 8: Convert the difference
        // into the requested target unit.
        double convertedResult =
                targetUnit.convertFromBaseUnit(difference);

        // Step 9: Validate the final converted result.
        if (!Double.isFinite(convertedResult)) {
            throw new ArithmeticException(
                    "Converted subtraction result is not finite");
        }

        // Step 10: Return a NEW Quantity object.
        // Neither original object gets modified.
        return new Quantity<>(convertedResult, targetUnit);
    }


    // ==============================================
    // UC12: Division between two quantities
    // ==============================================

    /**
     * Divide this quantity by another quantity
     * of the same measurement category.
     *
     * Returns a dimensionless double ratio.
     *
     * Example:
     * 24 INCHES / 2 FEET = 1.0
     */
    public double divide(Quantity<U> other) {

        // Step 1: Reject a null divisor.
        if (other == null) {
            throw new IllegalArgumentException(
                    "Divisor quantity cannot be null");
        }

        // Step 2: Ensure both quantities belong
        // to the same measurement category.
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException(
                    "Cannot divide different measurement categories");
        }

        // Step 3: Convert both quantities
        // into the common base unit.
        double firstBase =
                this.unit.convertToBaseUnit(this.value);

        double secondBase =
                other.unit.convertToBaseUnit(other.value);

        // Step 4: Division by zero is not allowed.
        if (secondBase == 0.0) {
            throw new ArithmeticException(
                    "Cannot divide by zero quantity");
        }

        // Step 5: Divide the two measurements.
        //
        // Both have the same base unit,
        // so the units cancel each other.
        double ratio = firstBase / secondBase;

        // Step 6: Validate the ratio.
        if (!Double.isFinite(ratio)) {
            throw new ArithmeticException(
                    "Division result is not finite");
        }

        // Step 7: Return the numerical ratio.
        return ratio;
    }

}
