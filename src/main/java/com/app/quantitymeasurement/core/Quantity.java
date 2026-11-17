package com.app.quantitymeasurement.core;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

// Generic Quantity class.
//
// U can be LengthUnit, WeightUnit, VolumeUnit,
// or another type implementing IMeasurable.
public final class Quantity<U extends IMeasurable> {

    // Store the measurement value.
    private final double value;

    // Store its measurement unit.
    private final U unit;


    // ==========================================
    // Constructor
    // ==========================================

    public Quantity(double value, U unit) {

        // Reject NaN or infinity.
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Measurement must be a finite number");
        }

        // Reject a null unit.
        if (unit == null) {
            throw new IllegalArgumentException(
                    "Unit cannot be null");
        }

        // Initialize instance variables.
        this.value = value;
        this.unit = unit;
    }


    // ==========================================
    // Getters
    // ==========================================

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }


    // ==========================================
    // Equality - Existing UC10 functionality
    // ==========================================

    @Override
    public boolean equals(Object obj) {

        // Same object reference.
        if (this == obj) {
            return true;
        }

        // Reject null or objects from another class.
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // We know it is a Quantity,
        // but its unit type may be different.
        Quantity<?> other = (Quantity<?>) obj;

        // Never compare length with weight or volume.
        if (this.unit.getClass() != other.unit.getClass()) {
            return false;
        }

        // Ask each unit to provide its normalized value.
        BigDecimal first =
                this.unit.normalizedBaseValue(this.value);

        BigDecimal second =
                other.unit.normalizedBaseValue(other.value);

        // 0 means both normalized values are equal.
        return first.compareTo(second) == 0;
    }

    @Override
    public int hashCode() {

        // Include category and normalized value.
        // This is consistent with equals().
        return Objects.hash(
                unit.getClass(),
                unit.normalizedBaseValue(value)
        );
    }


    // ==========================================
    // Unit Conversion - Existing UC10 functionality
    // ==========================================

    public static <T extends IMeasurable> double convert(
            double value,
            T sourceUnit,
            T targetUnit) {

        // Check numerical input.
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Measurement must be a finite number");
        }

        // Check both units.
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Units cannot be null");
        }

        // Check measurement category.
        if (sourceUnit.getClass() != targetUnit.getClass()) {
            throw new IllegalArgumentException(
                    "Incompatible measurement categories");
        }

        // No conversion needed for the same unit.
        if (sourceUnit == targetUnit) {
            return value;
        }

        // Convert into the category's base unit.
        double baseValue =
                sourceUnit.convertToBaseUnit(value);

        // Convert base value into requested unit.
        double result =
                targetUnit.convertFromBaseUnit(baseValue);

        // Reject invalid results.
        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Conversion result is not finite");
        }

        return result;
    }


    // Convert this Quantity into another unit.
    // Always return a new object.
    public Quantity<U> convertTo(U targetUnit) {

        double convertedValue = convert(
                this.value,
                this.unit,
                targetUnit
        );

        return new Quantity<>(convertedValue, targetUnit);
    }


    // ==========================================
    // UC13: Arithmetic Operation Enum
    // ==========================================

    // Private enum is used only inside Quantity.
    private enum ArithmeticOperation {

        // Addition calculation.
        ADD((a, b) -> a + b),

        // Subtraction calculation.
        SUBTRACT((a, b) -> a - b),

        // Division calculation.
        DIVIDE((a, b) -> {

            // Division by zero is not allowed.
            if (b == 0.0) {
                throw new ArithmeticException(
                        "Cannot divide by zero quantity");
            }

            return a / b;
        });

        // Store the lambda for each operation.
        private final DoubleBinaryOperator operator;

        // Enum constructor.
        ArithmeticOperation(DoubleBinaryOperator operator) {
            this.operator = operator;
        }

        // Execute the selected operation.
        double compute(double a, double b) {
            return operator.applyAsDouble(a, b);
        }
    }


    // ==========================================
    // UC13: Centralized Validation Helper
    // ==========================================

    /**
     * Validate operands before arithmetic.
     *
     * @param other second measurement
     * @param targetUnit requested result unit
     * @param targetUnitRequired true for add/subtract
     */
    private void validateArithmeticOperands(
            Quantity<U> other,
            U targetUnit,
            boolean targetUnitRequired) {

        // Check second operand.
        if (other == null) {
            throw new IllegalArgumentException(
                    "Second quantity cannot be null");
        }

        // Check whether target unit is required.
        if (targetUnitRequired && targetUnit == null) {
            throw new IllegalArgumentException(
                    "Target unit cannot be null");
        }

        // Check both measurement categories.
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException(
                    "Incompatible measurement categories");
        }

        // If target unit is specified,
        // it must belong to the same category.
        if (targetUnit != null
                && this.unit.getClass() != targetUnit.getClass()) {

            throw new IllegalArgumentException(
                    "Incompatible measurement categories");
        }

        // Validate both numerical values.
        if (!Double.isFinite(this.value)
                || !Double.isFinite(other.value)) {

            throw new IllegalArgumentException(
                    "Measurements must be finite numbers");
        }
    }


    // ==========================================
    // UC13: Centralized Arithmetic Helper
    // ==========================================

    /**
     * Convert both operands into base units,
     * execute the selected arithmetic operation,
     * and return its base-unit result.
     */

    private double performBaseArithmetic(
            Quantity<U> other,
            ArithmeticOperation operation) {

        // Step 1: Check whether the second operand exists
        // and whether both units belong to the same category.
        //
        // This is the UC13 centralized validation helper.
        validateArithmeticOperands(other, null, false);

        // Step 2: NEW UC14 CHECK.
        //
        // Before calculating anything, ask the unit
        // whether this arithmetic operation is supported.
        //
        // For LengthUnit, WeightUnit and VolumeUnit:
        // the default implementation allows the operation.
        //
        // For TemperatureUnit:
        // UnsupportedOperationException is thrown.
        this.unit.validateOperationSupport(operation.name());

        // Step 3: Convert both measurements
        // into their common base unit.
        double firstBase =
                this.unit.convertToBaseUnit(this.value);

        double secondBase =
                other.unit.convertToBaseUnit(other.value);

        // Step 4: Validate converted measurements.
        if (!Double.isFinite(firstBase)
                || !Double.isFinite(secondBase)) {

            throw new ArithmeticException(
                    "Base-unit conversion result is not finite");
        }

        // Step 5: Execute ADD, SUBTRACT, or DIVIDE
        // using the existing UC13 ArithmeticOperation enum.
        double result =
                operation.compute(firstBase, secondBase);

        // Step 6: Reject invalid arithmetic results.
        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Arithmetic result is not finite");
        }

        return result;
    }



    // ==========================================
    // UC13: Shared Result Creation
    // ==========================================

    /**
     * Execute add/subtract and express the result
     * in the requested target unit.
     */
    private Quantity<U> calculateQuantity(
            Quantity<U> other,
            U targetUnit,
            ArithmeticOperation operation) {

        // Check operand and target unit.
        validateArithmeticOperands(other, targetUnit, true);

        // Perform calculation using base-unit values.
        double baseResult =
                performBaseArithmetic(other, operation);

        // Convert final result into requested unit.
        double converted =
                targetUnit.convertFromBaseUnit(baseResult);

        // Reject an invalid converted result.
        if (!Double.isFinite(converted)) {
            throw new ArithmeticException(
                    "Converted arithmetic result is not finite");
        }

        // Return a NEW Quantity.
        // Both original objects stay unchanged.
        return new Quantity<>(converted, targetUnit);
    }


    // ==========================================
    // UC13: Refactored Addition
    // ==========================================

    // Existing UC6-style method.
    // Result uses the first object's unit.
    public Quantity<U> add(Quantity<U> other) {

        return add(other, this.unit);
    }

    // Existing UC7-style method.
    // Result uses the requested target unit.
    public Quantity<U> add(
            Quantity<U> other,
            U targetUnit) {

        // No repeated validation or conversion code.
        return calculateQuantity(
                other,
                targetUnit,
                ArithmeticOperation.ADD
        );
    }

    // Preserve existing static addition API.
    public static <T extends IMeasurable> Quantity<T> add(
            Quantity<T> first,
            Quantity<T> second,
            T targetUnit) {

        // Check the first object before calling a method on it.
        if (first == null) {
            throw new IllegalArgumentException(
                    "First quantity cannot be null");
        }

        return first.add(second, targetUnit);
    }


    // ==========================================
    // UC13: Refactored Subtraction
    // ==========================================

    // Result uses the first object's unit.
    public Quantity<U> subtract(Quantity<U> other) {

        return subtract(other, this.unit);
    }

    // Result uses the requested target unit.
    public Quantity<U> subtract(
            Quantity<U> other,
            U targetUnit) {

        return calculateQuantity(
                other,
                targetUnit,
                ArithmeticOperation.SUBTRACT
        );
    }


    // ==========================================
    // UC13: Refactored Division
    // ==========================================

    // Division returns a dimensionless double.
    public double divide(Quantity<U> other) {

        // The helper already handles validation,
        // conversion and division by zero.
        return performBaseArithmetic(
                other,
                ArithmeticOperation.DIVIDE
        );
    }


    // ==========================================
    // Display
    // ==========================================

    @Override
    public String toString() {

        return value + " " + unit.getUnitName();
    }
}
