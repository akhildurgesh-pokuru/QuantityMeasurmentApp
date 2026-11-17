
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityUC10Test {

    private static final double EPSILON = 1e-6;

    @Test
    void testInterface_LengthUnitImplementation() {
        IMeasurable unit = LengthUnit.FEET;

        assertEquals(
                5.0,
                unit.convertToBaseUnit(5.0),
                EPSILON
        );
    }

    @Test
    void testInterface_WeightUnitImplementation() {
        IMeasurable unit = WeightUnit.GRAM;

        assertEquals(1000.0,
                unit.convertFromBaseUnit(1.0),
                EPSILON);
    }

    @Test
    void testGenericQuantity_LengthEquality() {
        Quantity<LengthUnit> feet =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(12.0, LengthUnit.INCHES);

        assertEquals(feet, inches);
    }

    @Test
    void testGenericQuantity_WeightEquality() {
        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> grams =
                new Quantity<>(1000.0, WeightUnit.GRAM);

        assertEquals(kg, grams);
    }

    @Test
    void testGenericQuantity_LengthConversion() {
        Quantity<LengthUnit> feet =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> result =
                feet.convertTo(LengthUnit.INCHES);

        assertEquals(12.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    @Test
    void testGenericQuantity_WeightConversion() {
        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> result =
                kg.convertTo(WeightUnit.GRAM);

        assertEquals(1000.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }

    @Test
    void testGenericQuantity_LengthAddition() {
        Quantity<LengthUnit> feet =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(12.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result =
                feet.add(inches, LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testGenericQuantity_WeightAddition() {
        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> grams =
                new Quantity<>(1000.0, WeightUnit.GRAM);

        Quantity<WeightUnit> result =
                kg.add(grams, WeightUnit.KILOGRAM);

        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testCrossCategoryPrevention() {
        Quantity<LengthUnit> feet =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertNotEquals(feet, kg);
    }

    @Test
    void testConstructor_NullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, null));
    }

    @Test
    void testConstructor_InvalidValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(
                        Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testEquality_SymmetricAndHashCode() {
        Quantity<LengthUnit> feet =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(12.0, LengthUnit.INCHES);

        assertAll(
                () -> assertEquals(feet, inches),
                () -> assertEquals(inches, feet),
                () -> assertEquals(
                        feet.hashCode(), inches.hashCode())
        );
    }

    @Test
    void testAddition_OriginalObjectsUnchanged() {
        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> grams =
                new Quantity<>(500.0, WeightUnit.GRAM);

        Quantity<WeightUnit> result = kg.add(grams);

        assertAll(
                () -> assertEquals(1.0, kg.getValue(), EPSILON),
                () -> assertEquals(500.0, grams.getValue(), EPSILON),
                () -> assertEquals(1.5, result.getValue(), EPSILON),
                () -> assertNotSame(kg, result)
        );
    }

    @Test
    void testGenericQuantity_SameReference() {
        Quantity<LengthUnit> feet =
                new Quantity<>(1.0, LengthUnit.FEET);

        assertEquals(feet, feet);
    }

    @Test
    void testGenericQuantity_NullComparison() {
        Quantity<WeightUnit> kg =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertNotEquals(kg, null);
    }
}
