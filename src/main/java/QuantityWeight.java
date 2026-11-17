
import java.math.BigDecimal;
import java.math.MathContext;

// UC9: Class to represent a weight measurement.
// Supports equality, conversion, and addition.
public final class QuantityWeight {

    // Store the numerical measurement.
    private final double value;

    // Store the unit, such as KILOGRAM, GRAM, or POUND.
    private final WeightUnit unit;


    // ==========================================
    // Constructor
    // ==========================================

    public QuantityWeight(double value, WeightUnit unit) {

        // Reject NaN and infinity.
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }

        // Reject a missing measurement unit.
        if (unit == null) {
            throw new IllegalArgumentException(
                    "Weight unit cannot be null");
        }

        // Store the values inside the current object.
        this.value = value;
        this.unit = unit;
    }


    // ==========================================
    // Getter Methods
    // ==========================================

    // Return the numerical weight.
    public double getValue() {
        return value;
    }

    // Return the measurement unit.
    public WeightUnit getUnit() {
        return unit;
    }


    // ==========================================
    // UC9: Equality Comparison
    // ==========================================

    @Override
    public boolean equals(Object obj) {

        // If both references point to the same object,
        // they are equal.
        if (this == obj) {
            return true;
        }

        // Reject null or objects belonging to
        // another class, such as QuantityLength.
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Java knows obj belongs to QuantityWeight
        // after the previous type check.
        QuantityWeight other = (QuantityWeight) obj;

        // Convert both weights into normalized kilograms.
        // This allows 1 KILOGRAM and 1000 GRAM
        // to be compared correctly.
        BigDecimal firstWeight =
                this.unit.normalizedKilograms(this.value);

        BigDecimal secondWeight =
                other.unit.normalizedKilograms(other.value);

        // BigDecimal.compareTo() returns 0
        // when both values are numerically equal.
        return firstWeight.compareTo(secondWeight) == 0;
    }


    // ==========================================
    // UC9: Hash Code
    // ==========================================

    @Override
    public int hashCode() {

        // Equal quantities must have equal hash codes.
        // Use the same normalized representation
        // that equals() uses.
        return unit.normalizedKilograms(value).hashCode();
    }


    // ==========================================
    // UC9: Static Unit Conversion
    // ==========================================

    public static double convert(
            double value,
            WeightUnit sourceUnit,
            WeightUnit targetUnit) {

        // Validate the number.
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }

        // Both source and target units are required.
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Weight units cannot be null");
        }

        // If both units are the same,
        // return the original value.
        if (sourceUnit == targetUnit) {
            return value;
        }

        // Step 1: Convert source measurement
        // into the common base unit: KILOGRAM.
        double kilograms =
                sourceUnit.convertToBaseUnit(value);

        // Step 2: Convert kilograms
        // into the requested target unit.
        double result =
                targetUnit.convertFromBaseUnit(kilograms);

        // Check whether the converted value is valid.
        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Converted weight is outside double range");
        }

        return result;
    }


    // ==========================================
    // UC9: Instance Unit Conversion
    // ==========================================

    public QuantityWeight convertTo(WeightUnit targetUnit) {

        // Convert the current object's measurement.
        double convertedValue = convert(
                this.value,
                this.unit,
                targetUnit
        );

        // Return a NEW object.
        // The original object remains unchanged.
        return new QuantityWeight(
                convertedValue,
                targetUnit
        );
    }


    // ==========================================
    // UC9: Addition
    // Result in the first object's unit
    // ==========================================

    public QuantityWeight add(QuantityWeight other) {

        // Check that the second object exists.
        if (other == null) {
            throw new IllegalArgumentException(
                    "Second weight cannot be null");
        }

        // 'this' represents the object calling add().
        //
        // Example:
        // kg.add(grams)
        //
        // this = kg
        // other = grams
        //
        // Return the result in this.unit.
        return add(this, other, this.unit);
    }


    // ==========================================
    // UC9: Addition With Target Unit
    // ==========================================

    public QuantityWeight add(
            QuantityWeight other,
            WeightUnit targetUnit) {

        // Delegate to the static addition method.
        return add(this, other, targetUnit);
    }


    // ==========================================
    // UC9: Static Addition of Two Objects
    // ==========================================

    public static QuantityWeight add(
            QuantityWeight first,
            QuantityWeight second,
            WeightUnit targetUnit) {

        // Validate both objects and the requested unit.
        if (first == null || second == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Weights and target unit cannot be null");
        }

        // Step 1: Convert the first weight into kilograms.
        BigDecimal firstKg = BigDecimal.valueOf(first.value)
                .multiply(first.unit.getConversionFactor());

        // Step 2: Convert the second weight into kilograms.
        BigDecimal secondKg = BigDecimal.valueOf(second.value)
                .multiply(second.unit.getConversionFactor());

        // Step 3: Add both weights in kilograms.
        BigDecimal totalKg = firstKg.add(secondKg);

        // Step 4: Convert the total kilograms
        // into the requested target unit.
        BigDecimal convertedResult = totalKg.divide(
                targetUnit.getConversionFactor(),
                MathContext.DECIMAL64
        );

        // Step 5: Convert the result into double.
        double result = convertedResult.doubleValue();

        // Reject overflow results.
        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Addition result is outside double range");
        }

        // Step 6: Return a new immutable object.
        return new QuantityWeight(result, targetUnit);
    }


    // ==========================================
    // UC9: Overloaded Addition Method
    // ==========================================

    // This method accepts raw numbers and units
    // instead of existing QuantityWeight objects.
    public static QuantityWeight add(
            double value1,
            WeightUnit unit1,
            double value2,
            WeightUnit unit2,
            WeightUnit targetUnit) {

        // Create an object for the first measurement.
        QuantityWeight first =
                new QuantityWeight(value1, unit1);

        // Create an object for the second measurement.
        QuantityWeight second =
                new QuantityWeight(value2, unit2);

        // Reuse the existing static addition method.
        return add(first, second, targetUnit);
    }


    // ==========================================
    // Display the Measurement
    // ==========================================

    @Override
    public String toString() {

        // Example: "1.5 KILOGRAM"
        return value + " " + unit;
    }
}
