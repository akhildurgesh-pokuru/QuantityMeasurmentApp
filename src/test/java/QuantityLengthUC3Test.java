
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthUC3Test {

    private static final double EPSILON = 1e-6;

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(2.0, LengthUnit.FEET);

        assertEquals(
                new QuantityLength(3.0, LengthUnit.FEET),
                q1.add(q2));
    }

    @Test
    void testAddition_SameUnit_InchPlusInch() {
        QuantityLength q1 = new QuantityLength(6.0, LengthUnit.INCHES);
        QuantityLength q2 = new QuantityLength(6.0, LengthUnit.INCHES);

        assertEquals(
                new QuantityLength(12.0, LengthUnit.INCHES),
                q1.add(q2));
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCHES);

        assertEquals(
                new QuantityLength(2.0, LengthUnit.FEET),
                feet.add(inches));
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);

        assertEquals(
                new QuantityLength(24.0, LengthUnit.INCHES),
                inches.add(feet));
    }

    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        QuantityLength yards = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);

        assertEquals(
                new QuantityLength(2.0, LengthUnit.YARDS),
                yards.add(feet));
    }

    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        QuantityLength cm = new QuantityLength(
                2.54, LengthUnit.CENTIMETERS);
        QuantityLength inch = new QuantityLength(
                1.0, LengthUnit.INCHES);

        QuantityLength result = cm.add(inch);

        assertEquals(
                new QuantityLength(5.08, LengthUnit.CENTIMETERS),
                result);
    }

    @Test
    void testAddition_Commutativity() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCHES);

        assertEquals(feet.add(inches), inches.add(feet));
    }

    @Test
    void testAddition_WithZero() {
        QuantityLength feet = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength zero = new QuantityLength(0.0, LengthUnit.INCHES);

        assertEquals(feet, feet.add(zero));
    }

    @Test
    void testAddition_NegativeValues() {
        QuantityLength q1 = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(-2.0, LengthUnit.FEET);

        assertEquals(
                new QuantityLength(3.0, LengthUnit.FEET),
                q1.add(q2));
    }

    @Test
    void testAddition_NullSecondOperand() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class,
                () -> feet.add(null));
    }

    @Test
    void testAddition_LargeValues() {
        QuantityLength q1 = new QuantityLength(1e6, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(1e6, LengthUnit.FEET);

        assertEquals(
                new QuantityLength(2e6, LengthUnit.FEET),
                q1.add(q2));
    }

    @Test
    void testAddition_SmallValues() {
        QuantityLength q1 = new QuantityLength(0.001, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(0.002, LengthUnit.FEET);

        assertEquals(
                new QuantityLength(0.003, LengthUnit.FEET),
                q1.add(q2));
    }

    @Test
    void testAddition_ExplicitTargetUnit() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = QuantityLength.add(
                feet, inches, LengthUnit.YARDS);

        assertEquals(
                2.0 / 3.0,
                QuantityLength.convert(
                        2.0, LengthUnit.FEET, LengthUnit.YARDS),
                EPSILON);

        assertEquals(
                new QuantityLength(2.0, LengthUnit.FEET),
                result);
    }

    @Test
    void testAddition_NullTargetUnit() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.add(q1, q2, null));
    }

    @Test
    void testAddition_OriginalObjectsUnchanged() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = feet.add(inches);

        assertAll(
                () -> assertEquals(
                        new QuantityLength(1.0, LengthUnit.FEET), feet),
                () -> assertEquals(
                        new QuantityLength(12.0, LengthUnit.INCHES), inches),
                () -> assertNotSame(feet, result),
                () -> assertNotSame(inches, result)
        );
    }
}
