

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    // Feet: Same values
    @Test
    void testFeetEquality_SameValue() {

        assertTrue(QuantityMeasurementApp.compareFeet(1.0, 1.0));
    }

    // Feet: Different values
    @Test
    void testFeetEquality_DifferentValue() {

        assertFalse(QuantityMeasurementApp.compareFeet(1.0, 2.0));
    }

    // Inches: Same values
    @Test
    void testInchesEquality_SameValue() {

        assertTrue(QuantityMeasurementApp.compareInches(1.0, 1.0));
    }

    // Inches: Different values
    @Test
    void testInchesEquality_DifferentValue() {

        assertFalse(QuantityMeasurementApp.compareInches(1.0, 2.0));
    }

    // Feet: Null
    @Test
    void testFeetEquality_NullComparison() {

        QuantityMeasurementApp.Feet feet =
                new QuantityMeasurementApp.Feet(1.0);

        assertFalse(feet.equals(null));
    }

    // Inches: Null
    @Test
    void testInchesEquality_NullComparison() {

        QuantityMeasurementApp.Inches inches =
                new QuantityMeasurementApp.Inches(1.0);

        assertFalse(inches.equals(null));
    }

    // Feet: Non-numeric/different-type object
    @Test
    void testFeetEquality_NonNumericInput() {

        QuantityMeasurementApp.Feet feet =
                new QuantityMeasurementApp.Feet(1.0);

        assertFalse(feet.equals("abc"));
    }

    // Inches: Non-numeric/different-type object
    @Test
    void testInchesEquality_NonNumericInput() {

        QuantityMeasurementApp.Inches inches =
                new QuantityMeasurementApp.Inches(1.0);

        assertFalse(inches.equals("abc"));
    }

    // Feet: Same reference
    @Test
    void testFeetEquality_SameReference() {

        QuantityMeasurementApp.Feet feet =
                new QuantityMeasurementApp.Feet(1.0);

        assertTrue(feet.equals(feet));
    }

    // Inches: Same reference
    @Test
    void testInchesEquality_SameReference() {

        QuantityMeasurementApp.Inches inches =
                new QuantityMeasurementApp.Inches(1.0);

        assertTrue(inches.equals(inches));
    }

    // Different units must not compare equal
    @Test
    void testFeetAndInches_AreNotEqual() {

        QuantityMeasurementApp.Feet feet =
                new QuantityMeasurementApp.Feet(1.0);

        QuantityMeasurementApp.Inches inches =
                new QuantityMeasurementApp.Inches(1.0);

        assertFalse(feet.equals(inches));
    }
}
