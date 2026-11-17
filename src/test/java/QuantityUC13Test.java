
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityUC13Test {

    // Tolerance for numeric comparisons.
    private static final double EPSILON = 1e-6;


    @Test
    void testRefactoring_Addition() {

        Quantity<LengthUnit> feet =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(12.0, LengthUnit.INCHES);

        // 1 foot + 12 inches = 2 feet.
        Quantity<LengthUnit> result = feet.add(inches);

        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }


    @Test
    void testRefactoring_Subtraction() {

        Quantity<LengthUnit> feet =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(6.0, LengthUnit.INCHES);

        // 10 - 0.5 = 9.5 feet.
        Quantity<LengthUnit> result =
                feet.subtract(inches);

        assertEquals(9.5, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }


    @Test
    void testRefactoring_Division() {

        Quantity<LengthUnit> feet =
                new Quantity<>(24.0, LengthUnit.INCHES);

        Quantity<LengthUnit> other =
                new Quantity<>(2.0, LengthUnit.FEET);

        // Both values represent 2 feet.
        assertEquals(1.0, feet.divide(other), EPSILON);
    }


    @Test
    void testAddition_ExplicitTargetUnit() {

        Quantity<WeightUnit> kg =
                new Quantity<>(10.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> grams =
                new Quantity<>(5000.0, WeightUnit.GRAM);

        // 10 kg + 5 kg = 15000 grams.
        Quantity<WeightUnit> result =
                kg.add(grams, WeightUnit.GRAM);

        assertEquals(15000.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.GRAM, result.getUnit());
    }


    @Test
    void testSubtraction_ExplicitTargetUnit() {

        Quantity<LengthUnit> feet =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(6.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result =
                feet.subtract(inches, LengthUnit.INCHES);

        assertEquals(114.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }


    @Test
    void testDivision_ByZero() {

        Quantity<LengthUnit> feet =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> zero =
                new Quantity<>(0.0, LengthUnit.INCHES);

        // Division by zero must throw an exception.
        assertThrows(
                ArithmeticException.class,
                () -> feet.divide(zero)
        );
    }


    @Test
    void testValidation_NullOperand_ConsistentAcrossOperations() {

        Quantity<LengthUnit> feet =
                new Quantity<>(10.0, LengthUnit.FEET);

        // All operations use the same validation helper.
        IllegalArgumentException addError =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> feet.add(null)
                );

        IllegalArgumentException subtractError =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> feet.subtract(null)
                );

        IllegalArgumentException divideError =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> feet.divide(null)
                );

        // All three should report the same problem.
        assertEquals(addError.getMessage(),
                subtractError.getMessage());

        assertEquals(subtractError.getMessage(),
                divideError.getMessage());
    }


    @Test
    void testValidation_NullTargetUnit() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(5.0, LengthUnit.FEET);

        assertAll(
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> a.add(b, null)),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> a.subtract(b, null))
        );
    }


    @Test
    void testSubtraction_NegativeResult() {

        Quantity<WeightUnit> small =
                new Quantity<>(2.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> big =
                new Quantity<>(5.0, WeightUnit.KILOGRAM);

        // Subtraction preserves negative results.
        assertEquals(-3.0,
                small.subtract(big).getValue(),
                EPSILON);
    }


    @Test
    void testDivision_NonCommutative() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(5.0, LengthUnit.FEET);

        // Swapping operands changes the ratio.
        assertEquals(2.0, a.divide(b), EPSILON);
        assertEquals(0.5, b.divide(a), EPSILON);
    }


    @Test
    void testArithmetic_Volume() {

        Quantity<VolumeUnit> litres =
                new Quantity<>(5.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> ml =
                new Quantity<>(500.0, VolumeUnit.MILLILITRE);

        // One generic class handles Volume too.
        assertAll(
                () -> assertEquals(
                        5.5,
                        litres.add(ml).getValue(),
                        EPSILON),
                () -> assertEquals(
                        4.5,
                        litres.subtract(ml).getValue(),
                        EPSILON),
                () -> assertEquals(
                        10.0,
                        litres.divide(ml),
                        EPSILON)
        );
    }


    @Test
    void testArithmetic_Immutability() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(2.0, LengthUnit.FEET);

        // Operations must not modify the original objects.
        Quantity<LengthUnit> sum = a.add(b);
        Quantity<LengthUnit> difference = a.subtract(b);
        double ratio = a.divide(b);

        assertAll(
                () -> assertEquals(10.0, a.getValue(), EPSILON),
                () -> assertEquals(2.0, b.getValue(), EPSILON),
                () -> assertEquals(12.0, sum.getValue(), EPSILON),
                () -> assertEquals(8.0, difference.getValue(), EPSILON),
                () -> assertEquals(5.0, ratio, EPSILON),
                () -> assertNotSame(a, sum),
                () -> assertNotSame(a, difference)
        );
    }


    @Test
    void testArithmetic_ChainOperations() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(4.0, LengthUnit.FEET);

        Quantity<LengthUnit> c =
                new Quantity<>(2.0, LengthUnit.FEET);

        // (10 + 4 - 2) / 2 = 6.
        double result = a.add(b)
                .subtract(c)
                .divide(c);

        assertEquals(6.0, result, EPSILON);
    }


    @Test
    void testSubtraction_SmallValuePrecision() {

        Quantity<LengthUnit> a =
                new Quantity<>(0.001, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(0.0005, LengthUnit.FEET);

        // Preserve small values from UC12.
        double result = a.subtract(b).getValue();

        assertEquals(0.0005, result, 1e-10);
    }
}
