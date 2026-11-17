
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthTest {

    @Test
    void testEquality_FeetToFeet_SameValue() {
        QuantityLength q1 =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength q2 =
                new QuantityLength(1.0, LengthUnit.FEET);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        QuantityLength q1 =
                new QuantityLength(1.0, LengthUnit.INCHES);

        QuantityLength q2 =
                new QuantityLength(1.0, LengthUnit.INCHES);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        QuantityLength q1 =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength q2 =
                new QuantityLength(12.0, LengthUnit.INCHES);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        QuantityLength q1 =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength q2 =
                new QuantityLength(1.0, LengthUnit.FEET);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_DifferentValues() {
        QuantityLength q1 =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength q2 =
                new QuantityLength(2.0, LengthUnit.FEET);

        assertNotEquals(q1, q2);
    }

    @Test
    void testEquality_NullComparison() {
        QuantityLength q =
                new QuantityLength(1.0, LengthUnit.FEET);

        assertNotEquals(null, q);
    }

    @Test
    void testEquality_SameReference() {
        QuantityLength q =
                new QuantityLength(1.0, LengthUnit.FEET);

        assertEquals(q, q);
    }

    @Test
    void testEquality_InvalidUnit() {
        assertThrows(NullPointerException.class, () ->
                new QuantityLength(1.0, null)
        );
    }

    @Test
    void testEquality_DifferentType() {
        QuantityLength q =
                new QuantityLength(1.0, LengthUnit.FEET);

        assertNotEquals(q, "1.0 feet");
    }

}
