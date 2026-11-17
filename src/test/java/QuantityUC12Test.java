
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityUC12Test {

    // Allowed difference when checking double results.
    private static final double EPSILON = 1e-9;


    // Helper method to verify the value and unit
    // returned by length subtraction.
    private void assertLength(
            double expectedValue,
            LengthUnit expectedUnit,
            Quantity<LengthUnit> actual) {

        assertEquals(expectedValue, actual.getValue(), EPSILON);
        assertEquals(expectedUnit, actual.getUnit());
    }


    // ==========================================
    // SUBTRACTION TESTS
    // ==========================================

    @Test
    void testSubtraction_SameUnit_FeetMinusFeet() {

        Quantity<LengthUnit> first =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> second =
                new Quantity<>(5.0, LengthUnit.FEET);

        // 10 - 5 = 5 feet.
        assertLength(
                5.0, LengthUnit.FEET,
                first.subtract(second)
        );
    }

    @Test
    void testSubtraction_CrossUnit_FeetMinusInches() {

        Quantity<LengthUnit> feet =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(6.0, LengthUnit.INCHES);

        // 6 inches = 0.5 feet.
        // 10 - 0.5 = 9.5 feet.
        assertLength(
                9.5, LengthUnit.FEET,
                feet.subtract(inches)
        );
    }

    @Test
    void testSubtraction_CrossUnit_InchesMinusFeet() {

        Quantity<LengthUnit> inches =
                new Quantity<>(120.0, LengthUnit.INCHES);

        Quantity<LengthUnit> feet =
                new Quantity<>(5.0, LengthUnit.FEET);

        // 5 feet = 60 inches.
        // 120 - 60 = 60 inches.
        assertLength(
                60.0, LengthUnit.INCHES,
                inches.subtract(feet)
        );
    }

    @Test
    void testSubtraction_ExplicitTargetUnit() {

        Quantity<LengthUnit> feet =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(6.0, LengthUnit.INCHES);

        // Calculate the result in inches.
        assertLength(
                114.0, LengthUnit.INCHES,
                feet.subtract(inches, LengthUnit.INCHES)
        );
    }

    @Test
    void testSubtraction_NegativeResult() {

        Quantity<LengthUnit> small =
                new Quantity<>(5.0, LengthUnit.FEET);

        Quantity<LengthUnit> big =
                new Quantity<>(10.0, LengthUnit.FEET);

        // 5 - 10 = -5.
        assertLength(
                -5.0, LengthUnit.FEET,
                small.subtract(big)
        );
    }

    @Test
    void testSubtraction_ZeroResult() {

        Quantity<LengthUnit> feet =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> inches =
                new Quantity<>(120.0, LengthUnit.INCHES);

        // Both values represent the same length.
        assertLength(
                0.0, LengthUnit.FEET,
                feet.subtract(inches)
        );
    }

    @Test
    void testSubtraction_NegativeOperand() {

        Quantity<LengthUnit> first =
                new Quantity<>(5.0, LengthUnit.FEET);

        Quantity<LengthUnit> second =
                new Quantity<>(-2.0, LengthUnit.FEET);

        // 5 - (-2) = 7 feet.
        assertLength(
                7.0, LengthUnit.FEET,
                first.subtract(second)
        );
    }

    @Test
    void testSubtraction_SmallValues() {

        Quantity<LengthUnit> first =
                new Quantity<>(0.001, LengthUnit.FEET);

        Quantity<LengthUnit> second =
                new Quantity<>(0.0005, LengthUnit.FEET);

        // Check that small differences are preserved.
        assertLength(
                0.0005, LengthUnit.FEET,
                first.subtract(second)
        );
    }

    @Test
    void testSubtraction_NonCommutative() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(5.0, LengthUnit.FEET);

        // A - B = 5, but B - A = -5.
        assertAll(
                () -> assertLength(
                        5.0, LengthUnit.FEET, a.subtract(b)),
                () -> assertLength(
                        -5.0, LengthUnit.FEET, b.subtract(a))
        );
    }

    @Test
    void testSubtraction_NullOperand() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        // Cannot subtract a missing object.
        assertThrows(
                IllegalArgumentException.class,
                () -> a.subtract(null)
        );
    }

    @Test
    void testSubtraction_NullTargetUnit() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(5.0, LengthUnit.FEET);

        // Explicit target unit cannot be null.
        assertThrows(
                IllegalArgumentException.class,
                () -> a.subtract(b, null)
        );
    }

    @Test
    void testSubtraction_Weight() {

        Quantity<WeightUnit> kg =
                new Quantity<>(10.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> grams =
                new Quantity<>(5000.0, WeightUnit.GRAM);

        Quantity<WeightUnit> result = kg.subtract(grams);

        // 10 kg - 5 kg = 5 kg.
        assertEquals(5.0, result.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testSubtraction_VolumeWithTargetUnit() {

        Quantity<VolumeUnit> first =
                new Quantity<>(5.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> second =
                new Quantity<>(2.0, VolumeUnit.LITRE);

        // 3 litres = 3000 millilitres.
        Quantity<VolumeUnit> result =
                first.subtract(second, VolumeUnit.MILLILITRE);

        assertEquals(3000.0, result.getValue(), EPSILON);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    void testSubtraction_ChainedOperations() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(2.0, LengthUnit.FEET);

        Quantity<LengthUnit> c =
                new Quantity<>(1.0, LengthUnit.FEET);

        // (10 - 2) - 1 = 7.
        Quantity<LengthUnit> result =
                a.subtract(b).subtract(c);

        assertLength(7.0, LengthUnit.FEET, result);
    }


    // ==========================================
    // DIVISION TESTS
    // ==========================================

    @Test
    void testDivision_SameUnit_Feet() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(2.0, LengthUnit.FEET);

        // 10 / 2 = 5.
        assertEquals(5.0, a.divide(b), EPSILON);
    }

    @Test
    void testDivision_CrossUnit_Length() {

        Quantity<LengthUnit> inches =
                new Quantity<>(24.0, LengthUnit.INCHES);

        Quantity<LengthUnit> feet =
                new Quantity<>(2.0, LengthUnit.FEET);

        // 24 inches = 2 feet.
        // Ratio = 1.
        assertEquals(1.0, inches.divide(feet), EPSILON);
    }

    @Test
    void testDivision_CrossUnit_Weight() {

        Quantity<WeightUnit> kg =
                new Quantity<>(2.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> grams =
                new Quantity<>(2000.0, WeightUnit.GRAM);

        // 2 kilograms / 2 kilograms = 1.
        assertEquals(1.0, kg.divide(grams), EPSILON);
    }

    @Test
    void testDivision_CrossUnit_Volume() {

        Quantity<VolumeUnit> ml =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        Quantity<VolumeUnit> litre =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        assertEquals(1.0, ml.divide(litre), EPSILON);
    }

    @Test
    void testDivision_RatioLessThanOne() {

        Quantity<LengthUnit> a =
                new Quantity<>(5.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(10.0, LengthUnit.FEET);

        // 5 / 10 = 0.5.
        assertEquals(0.5, a.divide(b), EPSILON);
    }

    @Test
    void testDivision_NonCommutative() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(5.0, LengthUnit.FEET);

        // A / B = 2.
        // B / A = 0.5.
        assertAll(
                () -> assertEquals(2.0, a.divide(b), EPSILON),
                () -> assertEquals(0.5, b.divide(a), EPSILON)
        );
    }

    @Test
    void testDivision_ByZero() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> zero =
                new Quantity<>(0.0, LengthUnit.FEET);

        // Dividing by zero throws ArithmeticException.
        assertThrows(
                ArithmeticException.class,
                () -> a.divide(zero)
        );
    }

    @Test
    void testDivision_NullOperand() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        assertThrows(
                IllegalArgumentException.class,
                () -> a.divide(null)
        );
    }

    @Test
    void testDivision_LargeRatio() {

        Quantity<WeightUnit> a =
                new Quantity<>(1e6, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> b =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertEquals(1e6, a.divide(b), EPSILON);
    }

    @Test
    void testDivision_SmallRatio() {

        Quantity<WeightUnit> a =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> b =
                new Quantity<>(1e6, WeightUnit.KILOGRAM);

        assertEquals(1e-6, a.divide(b), EPSILON);
    }


    // ==========================================
    // INTEGRATION AND IMMUTABILITY TESTS
    // ==========================================

    @Test
    void testSubtractionAndDivision_Integration() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(4.0, LengthUnit.FEET);

        Quantity<LengthUnit> c =
                new Quantity<>(2.0, LengthUnit.FEET);

        // (10 - 4) / 2 = 3.
        assertEquals(
                3.0,
                a.subtract(b).divide(c),
                EPSILON
        );
    }

    @Test
    void testSubtraction_Immutability() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(5.0, LengthUnit.FEET);

        Quantity<LengthUnit> result = a.subtract(b);

        // Make sure original values remain unchanged.
        assertAll(
                () -> assertEquals(10.0, a.getValue(), EPSILON),
                () -> assertEquals(5.0, b.getValue(), EPSILON),
                () -> assertEquals(5.0, result.getValue(), EPSILON),
                () -> assertNotSame(a, result)
        );
    }

    @Test
    void testSubtractionAddition_Inverse() {

        Quantity<LengthUnit> a =
                new Quantity<>(10.0, LengthUnit.FEET);

        Quantity<LengthUnit> b =
                new Quantity<>(2.0, LengthUnit.FEET);

        // Add 2 and then subtract 2.
        // We should get the original measurement.
        Quantity<LengthUnit> result =
                a.add(b).subtract(b);

        assertLength(10.0, LengthUnit.FEET, result);
    }
}
