
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthUC5Test {

    private static final double EPSILON = 1e-6;

    @Test
    void testConversion_FeetToInches() {
        assertEquals(12.0,
                QuantityLength.convert(
                        1.0, LengthUnit.FEET, LengthUnit.INCHES),
                EPSILON);
    }

    @Test
    void testConversion_InchesToFeet() {
        assertEquals(2.0,
                QuantityLength.convert(
                        24.0, LengthUnit.INCHES, LengthUnit.FEET),
                EPSILON);
    }

    @Test
    void testConversion_YardsToInches() {
        assertEquals(36.0,
                QuantityLength.convert(
                        1.0, LengthUnit.YARDS, LengthUnit.INCHES),
                EPSILON);
    }

    @Test
    void testConversion_InchesToYards() {
        assertEquals(2.0,
                QuantityLength.convert(
                        72.0, LengthUnit.INCHES, LengthUnit.YARDS),
                EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches() {
        assertEquals(1.0,
                QuantityLength.convert(
                        2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES),
                EPSILON);
    }

    @Test
    void testConversion_FeetToYards() {
        assertEquals(2.0,
                QuantityLength.convert(
                        6.0, LengthUnit.FEET, LengthUnit.YARDS),
                EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0,
                QuantityLength.convert(
                        0.0, LengthUnit.FEET, LengthUnit.INCHES),
                EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-12.0,
                QuantityLength.convert(
                        -1.0, LengthUnit.FEET, LengthUnit.INCHES),
                EPSILON);
    }

    @Test
    void testConversion_SameUnit() {
        assertEquals(5.0,
                QuantityLength.convert(
                        5.0, LengthUnit.FEET, LengthUnit.FEET),
                EPSILON);
    }

    @Test
    void testConversion_RoundTrip() {

        double inches = QuantityLength.convert(
                3.5, LengthUnit.FEET, LengthUnit.INCHES);

        double feet = QuantityLength.convert(
                inches, LengthUnit.INCHES, LengthUnit.FEET);

        assertEquals(3.5, feet, EPSILON);
    }

    @Test
    void testConversion_NullSourceUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(
                        1.0, null, LengthUnit.FEET));
    }

    @Test
    void testConversion_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(
                        1.0, LengthUnit.FEET, null));
    }

    @Test
    void testConversion_NaN() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(
                        Double.NaN,
                        LengthUnit.FEET,
                        LengthUnit.INCHES));
    }

    @Test
    void testConversion_PositiveInfinity() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(
                        Double.POSITIVE_INFINITY,
                        LengthUnit.FEET,
                        LengthUnit.INCHES));
    }

    @Test
    void testConversion_NegativeInfinity() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(
                        Double.NEGATIVE_INFINITY,
                        LengthUnit.FEET,
                        LengthUnit.INCHES));
    }

    @Test
    void testConversion_InstanceMethod() {

        QuantityLength original =
                new QuantityLength(3.0, LengthUnit.YARDS);

        QuantityLength converted =
                original.convertTo(LengthUnit.FEET);

        assertEquals(
                new QuantityLength(9.0, LengthUnit.FEET),
                converted
        );
    }
}
