
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

public final class QuantityLength {

    // Instance variables
    private final double value;
    private final LengthUnit unit;

    // Constructor
    public QuantityLength(double value, LengthUnit unit) {

        // Validate numerical value
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Measurement must be a finite number");
        }

        // Validate unit
        this.unit = Objects.requireNonNull(
                unit, "Unit cannot be null");

        this.value = value;
    }

    // Get measurement value
    public double getValue() {
        return value;
    }

    // Get measurement unit
    public LengthUnit getUnit() {
        return unit;
    }

    // UC3 and UC4:
    // Convert measurement to the common base unit (inches)
    private BigDecimal toInches() {

        return BigDecimal.valueOf(value)
                .multiply(unit.getConversionFactor())
                .setScale(6, RoundingMode.HALF_UP);
    }

    // UC3 and UC4:
    // Compare two length measurements
    @Override
    public boolean equals(Object obj) {

        // Same object reference
        if (this == obj) {
            return true;
        }

        // Null or different class
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Cast Object reference to QuantityLength
        QuantityLength other = (QuantityLength) obj;

        // Compare measurements after converting to inches
        return this.toInches()
                .compareTo(other.toInches()) == 0;
    }

    // Hash code consistent with equals()
    @Override
    public int hashCode() {
        return toInches().hashCode();
    }

    // UC5:
    // Convert a numerical value between two length units
    public static double convert(
            double value,
            LengthUnit sourceUnit,
            LengthUnit targetUnit) {

        // Validate numerical input
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Value must be a finite number");
        }

        // Validate source and target units
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Source and target units cannot be null");
        }

        // Same-unit conversion
        if (sourceUnit == targetUnit) {
            return value;
        }

        // Step 1: Convert source measurement into inches
        BigDecimal baseValue = BigDecimal.valueOf(value)
                .multiply(sourceUnit.getConversionFactor());

        // Step 2: Convert inches into the target unit
        BigDecimal convertedValue = baseValue.divide(
                targetUnit.getConversionFactor(),
                MathContext.DECIMAL64
        );

        double result = convertedValue.doubleValue();

        // Validate converted result
        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Converted value is outside the double range");
        }

        return result;
    }

    // UC5:
    // Convert this quantity to another unit
    // Returns a new object without modifying the original
    public QuantityLength convertTo(LengthUnit targetUnit) {

        double convertedValue = convert(
                this.value,
                this.unit,
                targetUnit
        );

        return new QuantityLength(
                convertedValue,
                targetUnit
        );
    }

    // UC6:
    // Add another quantity
    // Result is in the unit of the first object
    public QuantityLength add(QuantityLength other) {

        // Validate second operand
        if (other == null) {
            throw new IllegalArgumentException(
                    "Second quantity cannot be null");
        }

        // Convert second quantity into first object's unit
        double convertedValue = convert(
                other.value,
                other.unit,
                this.unit
        );

        // Add the values
        double sum = this.value + convertedValue;

        // Validate addition result
        if (!Double.isFinite(sum)) {
            throw new ArithmeticException(
                    "Addition result is outside the double range");
        }

        // Return new immutable object
        return new QuantityLength(sum, this.unit);
    }

    // UC7:
    // Add two quantities using an explicitly specified target unit
    public static QuantityLength add(
            QuantityLength first,
            QuantityLength second,
            LengthUnit targetUnit) {

        // Validate inputs
        if (first == null || second == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Quantities and target unit cannot be null");
        }

        // Reuse UC6 addition
        QuantityLength sum = first.add(second);

        // Convert total into requested target unit
        return sum.convertTo(targetUnit);
    }

    // UC7:
    // Overloaded instance method
    public QuantityLength add(
            QuantityLength other,
            LengthUnit targetUnit) {

        return QuantityLength.add(this, other, targetUnit);
    }

    // UC7:
    // Overloaded static method using raw values and units
    public static QuantityLength add(
            double value1,
            LengthUnit unit1,
            double value2,
            LengthUnit unit2,
            LengthUnit targetUnit) {

        QuantityLength first =
                new QuantityLength(value1, unit1);

        QuantityLength second =
                new QuantityLength(value2, unit2);

        return add(first, second, targetUnit);
    }

    // Human-readable representation
    @Override
    public String toString() {
        return value + " " + unit;
    }
}
