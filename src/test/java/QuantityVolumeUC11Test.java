
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityVolumeUC11Test {

    // Tolerance for checking calculated double values.
    // This is used in numeric assertions, not equals().
    private static final double EPSILON = 1e-5;

    // Helper: checks BOTH the result value and its unit.
    private void assertVolume(
            double expectedValue,
            VolumeUnit expectedUnit,
            Quantity<VolumeUnit> actual) {

        assertAll(
                () -> assertEquals(
                        expectedValue,
                        actual.getValue(),
                        EPSILON
                ),
                () -> assertEquals(
                        expectedUnit,
                        actual.getUnit()
                )
        );
    }

    // --------------------------------
    // 1. Equality Test Cases
    // --------------------------------

    @Test
    void testEquality_LitreToLitre_SameValue() {

        // Create two objects with identical measurements.
        Quantity<VolumeUnit> first =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> second =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        // Same physical volumes must be equal.
        assertEquals(first, second);
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {

        Quantity<VolumeUnit> first =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> second =
                new Quantity<>(2.0, VolumeUnit.LITRE);

        // 1 litre and 2 litres are different.
        assertNotEquals(first, second);
    }

    @Test
    void testEquality_LitreToMillilitre() {

        Quantity<VolumeUnit> litre =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> ml =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        // 1 litre = 1000 millilitres.
        assertEquals(litre, ml);
    }

    @Test
    void testEquality_MillilitreToLitre() {

        Quantity<VolumeUnit> ml =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        Quantity<VolumeUnit> litre =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        // Verify equality works in the reverse direction.
        assertEquals(ml, litre);
    }

    @Test
    void testEquality_LitreToGallon() {

        Quantity<VolumeUnit> litres =
                new Quantity<>(3.78541, VolumeUnit.LITRE);

        Quantity<VolumeUnit> gallon =
                new Quantity<>(1.0, VolumeUnit.GALLON);

        // 1 US gallon is approximately 3.78541 litres.
        assertEquals(litres, gallon);
    }

    @Test
    void testEquality_RoundedGallonEquivalent() {

        Quantity<VolumeUnit> litre =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> gallon =
                new Quantity<>(0.264172, VolumeUnit.GALLON);

        // These are equal at our selected normalization precision.
        assertEquals(litre, gallon);
    }

    @Test
    void testEquality_SameReference() {

        Quantity<VolumeUnit> volume =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        // An object must equal itself.
        assertEquals(volume, volume);
    }

    @Test
    void testEquality_NullComparison() {

        Quantity<VolumeUnit> volume =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        // A valid quantity should not equal null.
        assertNotEquals(volume, null);
    }

    @Test
    void testEquality_ZeroValues() {

        Quantity<VolumeUnit> litre =
                new Quantity<>(0.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> ml =
                new Quantity<>(0.0, VolumeUnit.MILLILITRE);

        // Zero litres = zero millilitres.
        assertEquals(litre, ml);
    }

    @Test
    void testEquality_NegativeValues() {

        Quantity<VolumeUnit> litre =
                new Quantity<>(-1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> ml =
                new Quantity<>(-1000.0, VolumeUnit.MILLILITRE);

        // The generic API supports negative numbers.
        assertEquals(litre, ml);
    }

    // --------------------------------
    // 2. Conversion Test Cases
    // --------------------------------

    @Test
    void testConversion_LitreToMillilitre() {

        Quantity<VolumeUnit> litre =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        // Convert 1 litre into millilitres.
        Quantity<VolumeUnit> result =
                litre.convertTo(VolumeUnit.MILLILITRE);

        assertVolume(1000.0, VolumeUnit.MILLILITRE, result);
    }

    @Test
    void testConversion_MillilitreToLitre() {

        Quantity<VolumeUnit> ml =
                new Quantity<>(500.0, VolumeUnit.MILLILITRE);

        // 500 millilitres = 0.5 litres.
        Quantity<VolumeUnit> result =
                ml.convertTo(VolumeUnit.LITRE);

        assertVolume(0.5, VolumeUnit.LITRE, result);
    }

    @Test
    void testConversion_GallonToLitre() {

        Quantity<VolumeUnit> gallon =
                new Quantity<>(2.0, VolumeUnit.GALLON);

        // 2 gallons = 7.57082 litres.
        Quantity<VolumeUnit> result =
                gallon.convertTo(VolumeUnit.LITRE);

        assertVolume(7.57082, VolumeUnit.LITRE, result);
    }

    @Test
    void testConversion_LitreToGallon() {

        Quantity<VolumeUnit> litre =
                new Quantity<>(3.78541, VolumeUnit.LITRE);

        // Convert the equivalent of one gallon into gallons.
        Quantity<VolumeUnit> result =
                litre.convertTo(VolumeUnit.GALLON);

        assertVolume(1.0, VolumeUnit.GALLON, result);
    }

    @Test
    void testConversion_RoundTrip() {

        Quantity<VolumeUnit> original =
                new Quantity<>(1.5, VolumeUnit.LITRE);

        // Convert to millilitres and then back to litres.
        Quantity<VolumeUnit> result = original
                .convertTo(VolumeUnit.MILLILITRE)
                .convertTo(VolumeUnit.LITRE);

        assertVolume(1.5, VolumeUnit.LITRE, result);
    }

    // --------------------------------
    // 3. Addition Test Cases
    // --------------------------------

    @Test
    void testAddition_SameUnit() {

        Quantity<VolumeUnit> a =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> b =
                new Quantity<>(2.0, VolumeUnit.LITRE);

        // Same-unit addition needs no unit change.
        Quantity<VolumeUnit> result = a.add(b);

        assertVolume(3.0, VolumeUnit.LITRE, result);
    }

    @Test
    void testAddition_LitrePlusMillilitre() {

        Quantity<VolumeUnit> litre =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> ml =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        // Result is in the first object's unit: LITRE.
        Quantity<VolumeUnit> result = litre.add(ml);

        assertVolume(2.0, VolumeUnit.LITRE, result);
    }

    @Test
    void testAddition_MillilitrePlusLitre() {

        Quantity<VolumeUnit> ml =
                new Quantity<>(500.0, VolumeUnit.MILLILITRE);

        Quantity<VolumeUnit> litre =
                new Quantity<>(0.5, VolumeUnit.LITRE);

        // Result is in MILLILITRE, because ml is first.
        Quantity<VolumeUnit> result = ml.add(litre);

        assertVolume(1000.0, VolumeUnit.MILLILITRE, result);
    }

    @Test
    void testAddition_ExplicitTargetUnit() {

        Quantity<VolumeUnit> litre =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> ml =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        // Ask for the answer in millilitres.
        Quantity<VolumeUnit> result =
                litre.add(ml, VolumeUnit.MILLILITRE);

        assertVolume(2000.0, VolumeUnit.MILLILITRE, result);
    }

    @Test
    void testAddition_GallonPlusLitre() {

        Quantity<VolumeUnit> gallon =
                new Quantity<>(1.0, VolumeUnit.GALLON);

        Quantity<VolumeUnit> litre =
                new Quantity<>(3.78541, VolumeUnit.LITRE);

        // 1 gallon + 1 gallon equivalent = 2 gallons.
        Quantity<VolumeUnit> result = gallon.add(litre);

        assertVolume(2.0, VolumeUnit.GALLON, result);
    }

    @Test
    void testAddition_WithZero() {

        Quantity<VolumeUnit> litre =
                new Quantity<>(5.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> zero =
                new Quantity<>(0.0, VolumeUnit.MILLILITRE);

        // Adding zero should not change the amount.
        Quantity<VolumeUnit> result = litre.add(zero);

        assertVolume(5.0, VolumeUnit.LITRE, result);
    }

    @Test
    void testAddition_NegativeValue() {

        Quantity<VolumeUnit> litre =
                new Quantity<>(5.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> ml =
                new Quantity<>(-2000.0, VolumeUnit.MILLILITRE);

        // 5 litres + (-2 litres) = 3 litres.
        Quantity<VolumeUnit> result = litre.add(ml);

        assertVolume(3.0, VolumeUnit.LITRE, result);
    }

    @Test
    void testAddition_Immutability() {

        Quantity<VolumeUnit> first =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> second =
                new Quantity<>(500.0, VolumeUnit.MILLILITRE);

        // Addition must return a new object.
        Quantity<VolumeUnit> result = first.add(second);

        // Original objects should remain unchanged.
        assertAll(
                () -> assertVolume(
                        1.0, VolumeUnit.LITRE, first),
                () -> assertVolume(
                        500.0, VolumeUnit.MILLILITRE, second),
                () -> assertVolume(
                        1.5, VolumeUnit.LITRE, result),
                () -> assertNotSame(first, result)
        );
    }

    // --------------------------------
    // 4. Validation and Category Tests
    // --------------------------------

    @Test
    void testCrossCategory_VolumeVsLength() {

        Quantity<VolumeUnit> volume =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<LengthUnit> length =
                new Quantity<>(1.0, LengthUnit.FEET);

        // Volume and length are different categories.
        assertNotEquals(volume, length);
    }

    @Test
    void testCrossCategory_VolumeVsWeight() {

        Quantity<VolumeUnit> volume =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<WeightUnit> weight =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        // Volume and weight are different categories.
        assertNotEquals(volume, weight);
    }

    @Test
    void testValidation_NullUnit() {

        // Quantity constructor should reject missing units.
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<VolumeUnit>(1.0, null));
    }

    @Test
    void testValidation_InvalidValue() {

        // NaN is not a valid volume measurement.
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(
                        Double.NaN, VolumeUnit.LITRE));
    }

    @Test
    void testVolumeUnit_ConversionFactor() {

        // Check the conversion factor stored in the enum.
        assertEquals(
                0,
                VolumeUnit.GALLON.getConversionFactor()
                        .compareTo(new java.math.BigDecimal("3.78541"))
        );
    }

    @Test
    void testVolumeUnit_BaseConversion() {

        // Convert from millilitres into litres.
        assertEquals(
                1.0,
                VolumeUnit.MILLILITRE
                        .convertToBaseUnit(1000.0),
                EPSILON
        );

        // Convert from litres into millilitres.
        assertEquals(
                1000.0,
                VolumeUnit.MILLILITRE
                        .convertFromBaseUnit(1.0),
                EPSILON
        );
    }

    @Test
    void testEquality_HashCodeConsistency() {

        Quantity<VolumeUnit> litre =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> ml =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        // Equal objects must have identical hash codes.
        assertEquals(litre, ml);
        assertEquals(litre.hashCode(), ml.hashCode());
    }
}
