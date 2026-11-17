
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityWeightUC9Test {

    private static final double EPSILON = 1e-5;

    private void assertWeight(
            double expectedValue,
            WeightUnit expectedUnit,
            QuantityWeight actual) {

        assertAll(
                () -> assertEquals(
                        expectedValue, actual.getValue(), EPSILON),
                () -> assertEquals(
                        expectedUnit, actual.getUnit())
        );
    }

    @Test
    void testEquality_KilogramToKilogram_SameValue() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertEquals(a, b);
    }

    @Test
    void testEquality_KilogramToKilogram_DifferentValue() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(2.0, WeightUnit.KILOGRAM);

        assertNotEquals(a, b);
    }

    @Test
    void testEquality_KilogramToGram() {
        QuantityWeight kg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight gram = new QuantityWeight(1000.0, WeightUnit.GRAM);

        assertEquals(kg, gram);
    }

    @Test
    void testEquality_GramToKilogram() {
        QuantityWeight gram = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight kg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertEquals(gram, kg);
    }

    @Test
    void testEquality_KilogramToPound() {
        QuantityWeight kg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight lb = new QuantityWeight(2.20462, WeightUnit.POUND);

        assertEquals(kg, lb);
    }

    @Test
    void testEquality_PoundToGram() {
        QuantityWeight lb = new QuantityWeight(1.0, WeightUnit.POUND);
        QuantityWeight gram = new QuantityWeight(453.592, WeightUnit.GRAM);

        assertEquals(lb, gram);
    }

    @Test
    void testEquality_WeightVsLength() {
        QuantityWeight weight =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityLength length =
                new QuantityLength(1.0, LengthUnit.FEET);

        assertNotEquals(weight, length);
    }

    @Test
    void testEquality_NullComparison() {
        QuantityWeight weight =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertNotEquals(weight, null);
    }

    @Test
    void testEquality_SameReference() {
        QuantityWeight weight =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertEquals(weight, weight);
    }

    @Test
    void testEquality_SymmetricAndHashCode() {
        QuantityWeight kg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight gram = new QuantityWeight(1000.0, WeightUnit.GRAM);

        assertAll(
                () -> assertEquals(kg, gram),
                () -> assertEquals(gram, kg),
                () -> assertEquals(kg.hashCode(), gram.hashCode())
        );
    }

    @Test
    void testEquality_ZeroAndNegativeValues() {
        assertAll(
                () -> assertEquals(
                        new QuantityWeight(0.0, WeightUnit.KILOGRAM),
                        new QuantityWeight(0.0, WeightUnit.GRAM)),
                () -> assertEquals(
                        new QuantityWeight(-1.0, WeightUnit.KILOGRAM),
                        new QuantityWeight(-1000.0, WeightUnit.GRAM))
        );
    }

    @Test
    void testConversion_KilogramToGram() {
        QuantityWeight kg =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertWeight(1000.0, WeightUnit.GRAM,
                kg.convertTo(WeightUnit.GRAM));
    }

    @Test
    void testConversion_PoundToKilogram() {
        QuantityWeight pounds =
                new QuantityWeight(2.0, WeightUnit.POUND);

        assertWeight(0.907184, WeightUnit.KILOGRAM,
                pounds.convertTo(WeightUnit.KILOGRAM));
    }

    @Test
    void testConversion_GramToPound() {
        QuantityWeight grams =
                new QuantityWeight(500.0, WeightUnit.GRAM);

        assertWeight(1.10231, WeightUnit.POUND,
                grams.convertTo(WeightUnit.POUND));
    }

    @Test
    void testConversion_RoundTrip() {
        QuantityWeight original =
                new QuantityWeight(1.5, WeightUnit.KILOGRAM);

        QuantityWeight converted = original
                .convertTo(WeightUnit.GRAM)
                .convertTo(WeightUnit.KILOGRAM);

        assertWeight(1.5, WeightUnit.KILOGRAM, converted);
    }

    @Test
    void testAddition_SameUnit() {
        QuantityWeight first =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight second =
                new QuantityWeight(2.0, WeightUnit.KILOGRAM);

        assertWeight(3.0, WeightUnit.KILOGRAM,
                first.add(second));
    }

    @Test
    void testAddition_KilogramPlusGram() {
        QuantityWeight kg =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight gram =
                new QuantityWeight(500.0, WeightUnit.GRAM);

        assertWeight(1.5, WeightUnit.KILOGRAM,
                kg.add(gram));
    }

    @Test
    void testAddition_GramPlusKilogram() {
        QuantityWeight gram =
                new QuantityWeight(500.0, WeightUnit.GRAM);

        QuantityWeight kg =
                new QuantityWeight(0.5, WeightUnit.KILOGRAM);

        assertWeight(1000.0, WeightUnit.GRAM,
                gram.add(kg));
    }

    @Test
    void testAddition_ExplicitTargetUnit() {
        QuantityWeight kg =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight gram =
                new QuantityWeight(1000.0, WeightUnit.GRAM);

        assertWeight(2000.0, WeightUnit.GRAM,
                kg.add(gram, WeightUnit.GRAM));
    }

    @Test
    void testAddition_Commutativity() {
        QuantityWeight kg =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight gram =
                new QuantityWeight(1000.0, WeightUnit.GRAM);

        assertEquals(kg.add(gram), gram.add(kg));
    }

    @Test
    void testAddition_WithZero() {
        QuantityWeight kg =
                new QuantityWeight(5.0, WeightUnit.KILOGRAM);

        QuantityWeight zero =
                new QuantityWeight(0.0, WeightUnit.GRAM);

        assertEquals(kg, kg.add(zero));
    }

    @Test
    void testAddition_NegativeValues() {
        QuantityWeight kg =
                new QuantityWeight(5.0, WeightUnit.KILOGRAM);

        QuantityWeight grams =
                new QuantityWeight(-2000.0, WeightUnit.GRAM);

        assertWeight(3.0, WeightUnit.KILOGRAM,
                kg.add(grams));
    }

    @Test
    void testAddition_OriginalObjectsUnchanged() {
        QuantityWeight kg =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight gram =
                new QuantityWeight(500.0, WeightUnit.GRAM);

        QuantityWeight result = kg.add(gram);

        assertAll(
                () -> assertWeight(1.0, WeightUnit.KILOGRAM, kg),
                () -> assertWeight(500.0, WeightUnit.GRAM, gram),
                () -> assertNotSame(kg, result),
                () -> assertNotSame(gram, result)
        );
    }

    @Test
    void testValidation_NullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityWeight(1.0, null));
    }

    @Test
    void testValidation_InvalidNumbers() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new QuantityWeight(
                                Double.NaN, WeightUnit.KILOGRAM)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new QuantityWeight(
                                Double.POSITIVE_INFINITY,
                                WeightUnit.KILOGRAM))
        );
    }

    @Test
    void testValidation_NullSecondOperand() {
        QuantityWeight kg =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class,
                () -> kg.add(null));
    }

    @Test
    void testValidation_NullTargetUnit() {
        QuantityWeight kg =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class,
                () -> kg.convertTo(null));
    }
}
