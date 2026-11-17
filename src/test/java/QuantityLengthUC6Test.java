
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthUC6Test {

    private static final double EPSILON = 1e-6;

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {

        QuantityLength q1 =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength q2 =
                new QuantityLength(2.0, LengthUnit.FEET);

        assertEquals(
                new QuantityLength(3.0, LengthUnit.FEET),
                q1.add(q2)
        );
    }

    @Test
    void testAddition_SameUnit_InchPlusInch() {

        QuantityLength q1 =
                new QuantityLength(6.0, LengthUnit.INCHES);

        QuantityLength q2 =
                new QuantityLength(6.0, LengthUnit.INCHES);

        assertEquals(
                new QuantityLength(12.0, LengthUnit.INCHES),
                q1.add(q2)
        );
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {

        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = feet.add(inches);

        assertAll(
                () -> assertEquals(2.0, result.getValue(), EPSILON),
                () -> assertEquals(LengthUnit.FEET, result.getUnit())
        );
    }

    @Test
    void testAddition_CrossUnit_InchesPlusFeet() {

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength result = inches.add(feet);

        assertAll(
                () -> assertEquals(24.0, result.getValue(), EPSILON),
                () -> assertEquals(LengthUnit.INCHES, result.getUnit())
        );
    }

    @Test
    void testAddition_CrossUnit_YardsPlusFeet() {

        QuantityLength yards =
                new QuantityLength(1.0, LengthUnit.YARDS);

        QuantityLength feet =
                new QuantityLength(3.0, LengthUnit.FEET);

        QuantityLength result = yards.add(feet);

        assertAll(
                () -> assertEquals(2.0, result.getValue(), EPSILON),
                () -> assertEquals(LengthUnit.YARDS, result.getUnit())
        );
    }

    @Test
    void testAddition_CrossUnit_CentimetersPlusInches() {

        QuantityLength cm =
                new QuantityLength(2.54, LengthUnit.CENTIMETERS);

        QuantityLength inch =
                new QuantityLength(1.0, LengthUnit.INCHES);

        QuantityLength result = cm.add(inch);

        assertAll(
                () -> assertEquals(5.08, result.getValue(), EPSILON),
                () -> assertEquals(
                        LengthUnit.CENTIMETERS, result.getUnit())
        );
    }

    @Test
    void testAddition_Commutativity() {

        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        assertEquals(
                feet.add(inches),
                inches.add(feet)
        );
    }

    @Test
    void testAddition_WithZero() {

        QuantityLength feet =
                new QuantityLength(5.0, LengthUnit.FEET);

        QuantityLength zero =
                new QuantityLength(0.0, LengthUnit.INCHES);

        assertEquals(feet, feet.add(zero));
    }

    @Test
    void testAddition_NegativeValues() {

        QuantityLength q1 =
                new QuantityLength(5.0, LengthUnit.FEET);

        QuantityLength q2 =
                new QuantityLength(-2.0, LengthUnit.FEET);

        assertEquals(
                new QuantityLength(3.0, LengthUnit.FEET),
                q1.add(q2)
        );
    }

    @Test
    void testAddition_NullSecondOperand() {

        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        assertThrows(
                IllegalArgumentException.class,
                () -> feet.add(null)
        );
    }

    @Test
    void testAddition_LargeValues() {

        QuantityLength q1 =
                new QuantityLength(1e6, LengthUnit.FEET);

        QuantityLength q2 =
                new QuantityLength(1e6, LengthUnit.FEET);

        assertEquals(
                new QuantityLength(2e6, LengthUnit.FEET),
                q1.add(q2)
        );
    }

    @Test
    void testAddition_SmallValues() {

        QuantityLength q1 =
                new QuantityLength(0.001, LengthUnit.FEET);

        QuantityLength q2 =
                new QuantityLength(0.002, LengthUnit.FEET);

        QuantityLength result = q1.add(q2);

        assertEquals(0.003, result.getValue(), EPSILON);
    }

    @Test
    void testAddition_OriginalObjectsUnchanged() {

        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = feet.add(inches);

        assertAll(
                () -> assertEquals(
                        new QuantityLength(1.0, LengthUnit.FEET),
                        feet),
                () -> assertEquals(
                        new QuantityLength(12.0, LengthUnit.INCHES),
                        inches),
                () -> assertNotSame(feet, result),
                () -> assertNotSame(inches, result)
        );
    }
}
