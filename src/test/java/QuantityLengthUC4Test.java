
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthUC4Test {

    @Test
    void testEquality_YardToYard_SameValue() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.YARDS);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(2.0, LengthUnit.YARDS);

        assertNotEquals(q1, q2);
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_FeetToYard_EquivalentValue() {
        QuantityLength q1 = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.YARDS);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(36.0, LengthUnit.INCHES);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_InchesToYard_EquivalentValue() {
        QuantityLength q1 = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.YARDS);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_YardToFeet_NonEquivalentValue() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(2.0, LengthUnit.FEET);

        assertNotEquals(q1, q2);
    }

    @Test
    void testEquality_CentimetersToCentimeters_SameValue() {
        QuantityLength q1 = new QuantityLength(2.0, LengthUnit.CENTIMETERS);
        QuantityLength q2 = new QuantityLength(2.0, LengthUnit.CENTIMETERS);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_CentimetersToCentimeters_DifferentValue() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength q2 = new QuantityLength(2.0, LengthUnit.CENTIMETERS);

        assertNotEquals(q1, q2);
    }

    @Test
    void testEquality_CentimetersToInches_EquivalentValue() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength q2 = new QuantityLength(0.393701, LengthUnit.INCHES);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_InchesToCentimeters_EquivalentValue() {
        QuantityLength q1 = new QuantityLength(0.393701, LengthUnit.INCHES);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.CENTIMETERS);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_CentimetersToFeet_NonEquivalentValue() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);

        assertNotEquals(q1, q2);
    }

    @Test
    void testEquality_CentimetersToInches_ExactRelationship() {
        QuantityLength q1 = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.INCHES);

        assertEquals(q1, q2);
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        QuantityLength yards = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCHES);

        assertAll(
                () -> assertEquals(yards, feet),
                () -> assertEquals(feet, inches),
                () -> assertEquals(yards, inches)
        );
    }

    @Test
    void testEquality_YardWithNullUnit() {
        assertThrows(NullPointerException.class,
                () -> new QuantityLength(1.0, null));
    }

    @Test
    void testEquality_CentimetersWithNullUnit() {
        assertThrows(NullPointerException.class,
                () -> new QuantityLength(1.0, null));
    }

    @Test
    void testEquality_YardSameReference() {
        QuantityLength yards = new QuantityLength(1.0, LengthUnit.YARDS);

        assertEquals(yards, yards);
    }

    @Test
    void testEquality_YardNullComparison() {
        QuantityLength yards = new QuantityLength(1.0, LengthUnit.YARDS);

        assertNotEquals(yards, null);
    }

    @Test
    void testEquality_CentimetersSameReference() {
        QuantityLength cm = new QuantityLength(1.0, LengthUnit.CENTIMETERS);

        assertEquals(cm, cm);
    }

    @Test
    void testEquality_CentimetersNullComparison() {
        QuantityLength cm = new QuantityLength(1.0, LengthUnit.CENTIMETERS);

        assertNotEquals(cm, null);
    }

    @Test
    void testEquality_AllUnits_ComplexScenario() {
        QuantityLength yards = new QuantityLength(2.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(6.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(72.0, LengthUnit.INCHES);
        QuantityLength cm = new QuantityLength(182.88, LengthUnit.CENTIMETERS);

        assertAll(
                () -> assertEquals(yards, feet),
                () -> assertEquals(feet, inches),
                () -> assertEquals(inches, cm),
                () -> assertEquals(yards, cm)
        );
    }

}
