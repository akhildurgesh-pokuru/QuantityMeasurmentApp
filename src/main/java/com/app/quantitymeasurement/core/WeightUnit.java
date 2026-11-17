package com.app.quantitymeasurement.core;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public enum WeightUnit implements IMeasurable {

    // Weight units and their conversion factors to kilograms
    KILOGRAM("1"),
    GRAM("0.001"),
    POUND("0.453592");

    // Store conversion factor for each unit
    private final BigDecimal conversionFactor;

    // Enum constructor
    WeightUnit(String factor) {
        this.conversionFactor = new BigDecimal(factor);
    }

    // Return conversion factor relative to kilograms
    @Override
    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    // Convert the current unit into kilograms
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

    // Convert kilograms into the current unit
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

    // Normalize weight into kilograms
    // Used for equals() and hashCode()
    public BigDecimal normalizedKilograms(double value) {

        validate(value);

        return BigDecimal.valueOf(value)
                .multiply(conversionFactor)
                .setScale(5, RoundingMode.HALF_UP);
    }

    // Return the unit name
    @Override
    public String getUnitName() {
        return name();
    }

    // Required by IMeasurable interface
    @Override
    public BigDecimal normalizedBaseValue(double value) {
        return normalizedKilograms(value);
    }

    // Validate measurement value
    private static void validate(double value) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Weight must be a finite number");
        }
    }
}
