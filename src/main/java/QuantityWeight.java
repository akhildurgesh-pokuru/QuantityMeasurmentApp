
import java.math.BigDecimal;
import java.math.MathContext;

public final class QuantityWeight {

    private final double value;
    private final WeightUnit unit;

    // Constructor
    public QuantityWeight(double value, WeightUnit unit) {

<<<<<<< HEAD
        // Validate measurement
=======
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }

<<<<<<< HEAD
        // Validate unit
=======
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
        if (unit == null) {
            throw new IllegalArgumentException(
                    "Weight unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

<<<<<<< HEAD
    // Return measurement value
=======
    // Getters
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
    public double getValue() {
        return value;
    }

<<<<<<< HEAD
    // Return measurement unit
=======
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
    public WeightUnit getUnit() {
        return unit;
    }

<<<<<<< HEAD
    // UC9: Compare weight measurements
    @Override
    public boolean equals(Object obj) {

        // Same object reference
=======
    // UC9: Equality of weights
    @Override
    public boolean equals(Object obj) {

        // Same reference
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
        if (this == obj) {
            return true;
        }

<<<<<<< HEAD
        // Reject null or different class
=======
        // Reject null or another class
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

<<<<<<< HEAD
        // Cast Object reference to QuantityWeight
        QuantityWeight other = (QuantityWeight) obj;

        // Normalize both weights to kilograms
=======
        QuantityWeight other = (QuantityWeight) obj;

        // Convert both weights to normalized kilograms
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
        return this.unit.normalizedKilograms(this.value)
                .compareTo(
                        other.unit.normalizedKilograms(other.value)
                ) == 0;
    }

<<<<<<< HEAD
    // Hash code must be consistent with equals()
=======
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
    @Override
    public int hashCode() {
        return unit.normalizedKilograms(value).hashCode();
    }

<<<<<<< HEAD
    // UC9: Convert a numerical value between weight units
=======
    // UC9: Convert raw values between weight units
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
    public static double convert(
            double value,
            WeightUnit sourceUnit,
            WeightUnit targetUnit) {

<<<<<<< HEAD
        // Validate numerical input
=======
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }

<<<<<<< HEAD
        // Validate units
=======
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Weight units cannot be null");
        }

<<<<<<< HEAD
        // Same unit - no conversion needed
=======
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
        if (sourceUnit == targetUnit) {
            return value;
        }

<<<<<<< HEAD
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
=======
        // Source -> kilograms -> target
        double kilograms =
                sourceUnit.convertToBaseUnit(value);

        return targetUnit.convertFromBaseUnit(kilograms);
    }

    // UC9: Convert current weight to another unit
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
    public QuantityWeight convertTo(WeightUnit targetUnit) {

        double convertedValue = convert(
                this.value,
                this.unit,
                targetUnit
        );

        return new QuantityWeight(convertedValue, targetUnit);
    }

    // UC9: Add another weight
<<<<<<< HEAD
    // Result in first object's unit
=======
    // Result uses the first object's unit
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
    public QuantityWeight add(QuantityWeight other) {

        if (other == null) {
            throw new IllegalArgumentException(
                    "Second weight cannot be null");
        }

        return add(this, other, this.unit);
    }

<<<<<<< HEAD
    // UC9: Add with a specified target unit
=======
    // UC9: Add with explicitly specified target unit
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
    public QuantityWeight add(
            QuantityWeight other,
            WeightUnit targetUnit) {

        return add(this, other, targetUnit);
    }

<<<<<<< HEAD
    // Static addition method
=======
    // Static addition using two objects
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
    public static QuantityWeight add(
            QuantityWeight first,
            QuantityWeight second,
            WeightUnit targetUnit) {

<<<<<<< HEAD
        // Validate operands and target unit
=======
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
        if (first == null || second == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Weights and target unit cannot be null");
        }

<<<<<<< HEAD
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
=======
        // Convert both measurements to kilograms
        BigDecimal firstKg = BigDecimal.valueOf(first.value)
                .multiply(first.unit.getConversionFactor());

        BigDecimal secondKg = BigDecimal.valueOf(second.value)
                .multiply(second.unit.getConversionFactor());

        // Add in common base unit
        BigDecimal totalKg = firstKg.add(secondKg);

        // Convert total into requested unit
        BigDecimal result = totalKg.divide(
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
                targetUnit.getConversionFactor(),
                MathContext.DECIMAL64
        );

<<<<<<< HEAD
        double result = convertedResult.doubleValue();

        // Validate result
        if (!Double.isFinite(result)) {
=======
        double answer = result.doubleValue();

        if (!Double.isFinite(answer)) {
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
            throw new ArithmeticException(
                    "Addition result is outside double range");
        }

<<<<<<< HEAD
        // Return new object
        return new QuantityWeight(result, targetUnit);
    }

    // Overloaded addition using raw values
    public static QuantityWeight add(
            double value1,
            WeightUnit unit1,
            double value2,
            WeightUnit unit2,
=======
        return new QuantityWeight(answer, targetUnit);
    }

    // Addition using raw values
    public static QuantityWeight add(
            double value1, WeightUnit unit1,
            double value2, WeightUnit unit2,
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
            WeightUnit targetUnit) {

        QuantityWeight first =
                new QuantityWeight(value1, unit1);

        QuantityWeight second =
                new QuantityWeight(value2, unit2);

        return add(first, second, targetUnit);
    }

<<<<<<< HEAD
    // Display measurement
=======
>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
    @Override
    public String toString() {
        return value + " " + unit;
    }
}
