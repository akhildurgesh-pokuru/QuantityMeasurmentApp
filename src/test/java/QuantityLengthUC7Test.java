
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthUC7Test {

    private static final double EPSILON = 1e-6;

    // Helper method to verify both value and unit
    private void assertQuantity(
            double expectedValue,
            LengthUnit expectedUnit,
            QuantityLength actual) {

        assertEquals(expectedValue,
                actual.getValue(), EPSILON);

        assertEquals(expectedUnit,
                actual.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Feet() {

        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result =
                feet.add(inches, LengthUnit.FEET);

        assertQuantity(2.0, LengthUnit.FEET, result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Inches() {

        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result =
                feet.add(inches, LengthUnit.INCHES);

        assertQuantity(24.0, LengthUnit.INCHES, result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Yards() {

        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result =
                feet.add(inches, LengthUnit.YARDS);

        assertQuantity(2.0 / 3.0, LengthUnit.YARDS, result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {

        QuantityLength inch1 =
                new QuantityLength(1.0, LengthUnit.INCHES);

        QuantityLength inch2 =
                new QuantityLength(1.0, LengthUnit.INCHES);

        QuantityLength result =
                inch1.add(inch2, LengthUnit.CENTIMETERS);

        assertQuantity(5.08, LengthUnit.CENTIMETERS, result);
    }

    @Test
    void testAddition_Commutativity() {

        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result1 =
                feet.add(inches, LengthUnit.YARDS);

        QuantityLength result2 =
                inches.add(feet, LengthUnit.YARDS);

        assertEquals(result1, result2);
    }

    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {

        QuantityLength feet =
                new QuantityLength(5.0, LengthUnit.FEET);

        QuantityLength zero =
                new QuantityLength(0.0, LengthUnit.INCHES);

        QuantityLength result =
                feet.add(zero, LengthUnit.YARDS);

        assertQuantity(5.0 / 3.0, LengthUnit.YARDS, result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {

        QuantityLength feet1 =
                new QuantityLength(5.0, LengthUnit.FEET);

        QuantityLength feet2 =
                new QuantityLength(-2.0, LengthUnit.FEET);

        QuantityLength result =
                feet1.add(feet2, LengthUnit.INCHES);

        assertQuantity(36.0, LengthUnit.INCHES, result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit() {

        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        assertThrows(IllegalArgumentException.class,
                () -> feet.add(inches, null));
    }

    @Test
    void testAddition_ExplicitTargetUnit_LargeValues() {

        QuantityLength feet1 =
                new QuantityLength(1000.0, LengthUnit.FEET);

        QuantityLength feet2 =
                new QuantityLength(500.0, LengthUnit.FEET);

        QuantityLength result =
                feet1.add(feet2, LengthUnit.INCHES);

        assertQuantity(18000.0, LengthUnit.INCHES, result);
    }

    @Test
    void testAddition_BackwardCompatibility_UC6() {

        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = feet.add(inches);

        assertQuantity(2.0, LengthUnit.FEET, result);
    }
}
