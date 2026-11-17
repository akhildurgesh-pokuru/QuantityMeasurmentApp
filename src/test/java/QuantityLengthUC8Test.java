
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthUC8Test {

    private static final double EPSILON = 1e-6;

    @Test
    void testInchesToBaseUnit() {
        assertEquals(1.0,
                LengthUnit.INCHES.convertToBaseUnit(12.0),
                EPSILON);
    }

    @Test
    void testYardsToBaseUnit() {
        assertEquals(3.0,
                LengthUnit.YARDS.convertToBaseUnit(1.0),
                EPSILON);
    }

    @Test
    void testCentimetersToBaseUnit() {
        assertEquals(1.0,
                LengthUnit.CENTIMETERS
                        .convertToBaseUnit(30.48),
                EPSILON);
    }

    @Test
    void testBaseUnitToInches() {
        assertEquals(12.0,
                LengthUnit.INCHES.convertFromBaseUnit(1.0),
                EPSILON);
    }

    @Test
    void testBaseUnitToYards() {
        assertEquals(1.0,
                LengthUnit.YARDS.convertFromBaseUnit(3.0),
                EPSILON);
    }

    @Test
    void testBaseUnitToCentimeters() {
        assertEquals(30.48,
                LengthUnit.CENTIMETERS
                        .convertFromBaseUnit(1.0),
                EPSILON);
    }

    @Test
    void testRefactoredEquality() {
        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        assertEquals(feet, inches);
    }

    @Test
    void testRefactoredConversion() {
        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength result =
                feet.convertTo(LengthUnit.INCHES);

        assertEquals(12.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    @Test
    void testRefactoredAddition() {
        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result =
                feet.add(inches, LengthUnit.YARDS);

        assertEquals(2.0 / 3.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.YARDS, result.getUnit());
    }

    @Test
    void testNullUnit() {
        assertThrows(NullPointerException.class,
                () -> new QuantityLength(1.0, null));
    }

    @Test
    void testInvalidValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(
                        Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testRoundTripConversion() {
        double inches = QuantityLength.convert(
                5.0, LengthUnit.FEET, LengthUnit.INCHES);

        double feet = QuantityLength.convert(
                inches, LengthUnit.INCHES, LengthUnit.FEET);

        assertEquals(5.0, feet, EPSILON);
    }
}
