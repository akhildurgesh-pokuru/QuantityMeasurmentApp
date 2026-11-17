
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityUC14Test {

    // Tolerance used for comparing floating-point results.
    private static final double EPSILON = 1e-6;


    // ==========================================
    // Temperature Equality Tests
    // ==========================================

    @Test
    void testEquality_CelsiusToCelsius() {

        Quantity<TemperatureUnit> first =
                new Quantity<>(25.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> second =
                new Quantity<>(25.0, TemperatureUnit.CELSIUS);

        // Same temperature values should be equal.
        assertEquals(first, second);
    }

    @Test
    void testEquality_CelsiusToFahrenheit() {

        Quantity<TemperatureUnit> celsius =
                new Quantity<>(0.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> fahrenheit =
                new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

        // 0 Celsius equals 32 Fahrenheit.
        assertEquals(celsius, fahrenheit);
    }

    @Test
    void testEquality_CelsiusToKelvin() {

        Quantity<TemperatureUnit> celsius =
                new Quantity<>(0.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> kelvin =
                new Quantity<>(273.15, TemperatureUnit.KELVIN);

        // 0 Celsius equals 273.15 Kelvin.
        assertEquals(celsius, kelvin);
    }

    @Test
    void testEquality_BoilingPoint() {

        Quantity<TemperatureUnit> celsius =
                new Quantity<>(100.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> fahrenheit =
                new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT);

        // Both represent water's boiling point
        // under standard atmospheric pressure.
        assertEquals(celsius, fahrenheit);
    }

    @Test
    void testEquality_NegativeForty() {

        Quantity<TemperatureUnit> celsius =
                new Quantity<>(-40.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> fahrenheit =
                new Quantity<>(-40.0, TemperatureUnit.FAHRENHEIT);

        // -40 Celsius equals -40 Fahrenheit.
        assertEquals(celsius, fahrenheit);
    }

    @Test
    void testEquality_HashCodeConsistency() {

        Quantity<TemperatureUnit> celsius =
                new Quantity<>(0.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> fahrenheit =
                new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

        // Equal objects must produce equal hash codes.
        assertEquals(celsius, fahrenheit);
        assertEquals(celsius.hashCode(), fahrenheit.hashCode());
    }

    @Test
    void testEquality_DifferentTemperatures() {

        Quantity<TemperatureUnit> first =
                new Quantity<>(50.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> second =
                new Quantity<>(100.0, TemperatureUnit.CELSIUS);

        assertNotEquals(first, second);
    }


    // ==========================================
    // Temperature Conversion Tests
    // ==========================================

    @Test
    void testConversion_CelsiusToFahrenheit() {

        Quantity<TemperatureUnit> celsius =
                new Quantity<>(100.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> result =
                celsius.convertTo(TemperatureUnit.FAHRENHEIT);

        // 100 Celsius = 212 Fahrenheit.
        assertEquals(212.0, result.getValue(), EPSILON);
        assertEquals(TemperatureUnit.FAHRENHEIT, result.getUnit());
    }

    @Test
    void testConversion_FahrenheitToCelsius() {

        Quantity<TemperatureUnit> fahrenheit =
                new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

        Quantity<TemperatureUnit> result =
                fahrenheit.convertTo(TemperatureUnit.CELSIUS);

        assertEquals(0.0, result.getValue(), EPSILON);
        assertEquals(TemperatureUnit.CELSIUS, result.getUnit());
    }

    @Test
    void testConversion_CelsiusToKelvin() {

        Quantity<TemperatureUnit> celsius =
                new Quantity<>(0.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> result =
                celsius.convertTo(TemperatureUnit.KELVIN);

        assertEquals(273.15, result.getValue(), EPSILON);
        assertEquals(TemperatureUnit.KELVIN, result.getUnit());
    }

    @Test
    void testConversion_KelvinToCelsius() {

        Quantity<TemperatureUnit> kelvin =
                new Quantity<>(273.15, TemperatureUnit.KELVIN);

        Quantity<TemperatureUnit> result =
                kelvin.convertTo(TemperatureUnit.CELSIUS);

        assertEquals(0.0, result.getValue(), EPSILON);
        assertEquals(TemperatureUnit.CELSIUS, result.getUnit());
    }

    @Test
    void testConversion_FahrenheitToKelvin() {

        Quantity<TemperatureUnit> fahrenheit =
                new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

        Quantity<TemperatureUnit> result =
                fahrenheit.convertTo(TemperatureUnit.KELVIN);

        assertEquals(273.15, result.getValue(), EPSILON);
    }

    @Test
    void testConversion_AbsoluteZero() {

        Quantity<TemperatureUnit> celsius =
                new Quantity<>(-273.15, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> result =
                celsius.convertTo(TemperatureUnit.KELVIN);

        // -273.15 Celsius = 0 Kelvin.
        assertEquals(0.0, result.getValue(), EPSILON);
    }

    @Test
    void testConversion_RoundTrip() {

        Quantity<TemperatureUnit> original =
                new Quantity<>(50.0, TemperatureUnit.CELSIUS);

        // Celsius -> Fahrenheit -> Celsius.
        Quantity<TemperatureUnit> result = original
                .convertTo(TemperatureUnit.FAHRENHEIT)
                .convertTo(TemperatureUnit.CELSIUS);

        assertEquals(50.0, result.getValue(), EPSILON);
    }


    // ==========================================
    // Arithmetic Support Tests
    // ==========================================

    @Test
    void testSupport_TemperatureAdditionDisabled() {

        assertFalse(TemperatureUnit.CELSIUS.supportsAddition());
    }

    @Test
    void testSupport_TemperatureSubtractionDisabled() {

        assertFalse(TemperatureUnit.CELSIUS.supportsSubtraction());
    }

    @Test
    void testSupport_TemperatureDivisionDisabled() {

        assertFalse(TemperatureUnit.FAHRENHEIT.supportsDivision());
    }

    @Test
    void testSupport_LengthAdditionEnabled() {

        // LengthUnit inherits the default true value.
        assertTrue(LengthUnit.FEET.supportsAddition());
    }

    @Test
    void testSupport_WeightDivisionEnabled() {

        // WeightUnit inherits the default true value.
        assertTrue(WeightUnit.KILOGRAM.supportsDivision());
    }

    @Test
    void testSupport_VolumeSubtractionEnabled() {

        assertTrue(VolumeUnit.LITRE.supportsSubtraction());
    }


    // ==========================================
    // Unsupported Operation Tests
    // ==========================================

    @Test
    void testUnsupported_Addition() {

        Quantity<TemperatureUnit> first =
                new Quantity<>(100.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> second =
                new Quantity<>(50.0, TemperatureUnit.CELSIUS);

        assertThrows(UnsupportedOperationException.class,
                () -> first.add(second));
    }

    @Test
    void testUnsupported_Subtraction() {

        Quantity<TemperatureUnit> first =
                new Quantity<>(100.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> second =
                new Quantity<>(50.0, TemperatureUnit.CELSIUS);

        assertThrows(UnsupportedOperationException.class,
                () -> first.subtract(second));
    }

    @Test
    void testUnsupported_Division() {

        Quantity<TemperatureUnit> first =
                new Quantity<>(100.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> second =
                new Quantity<>(50.0, TemperatureUnit.CELSIUS);

        assertThrows(UnsupportedOperationException.class,
                () -> first.divide(second));
    }

    @Test
    void testUnsupported_ErrorMessage() {

        Quantity<TemperatureUnit> first =
                new Quantity<>(100.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> second =
                new Quantity<>(50.0, TemperatureUnit.CELSIUS);

        UnsupportedOperationException exception =
                assertThrows(
                        UnsupportedOperationException.class,
                        () -> first.add(second)
                );

        assertTrue(exception.getMessage().contains("does not support"));
    }


    // ==========================================
    // Type Safety and Validation
    // ==========================================

    @Test
    void testCategory_TemperatureVsLength() {

        Quantity<TemperatureUnit> temperature =
                new Quantity<>(100.0, TemperatureUnit.CELSIUS);

        Quantity<LengthUnit> length =
                new Quantity<>(100.0, LengthUnit.FEET);

        assertNotEquals(temperature, length);
    }

    @Test
    void testCategory_TemperatureVsWeight() {

        Quantity<TemperatureUnit> temperature =
                new Quantity<>(50.0, TemperatureUnit.CELSIUS);

        Quantity<WeightUnit> weight =
                new Quantity<>(50.0, WeightUnit.KILOGRAM);

        assertNotEquals(temperature, weight);
    }

    @Test
    void testCategory_TemperatureVsVolume() {

        Quantity<TemperatureUnit> temperature =
                new Quantity<>(25.0, TemperatureUnit.CELSIUS);

        Quantity<VolumeUnit> volume =
                new Quantity<>(25.0, VolumeUnit.LITRE);

        assertNotEquals(temperature, volume);
    }

    @Test
    void testValidation_NullUnit() {

        // Quantity constructor rejects a null unit.
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<TemperatureUnit>(100.0, null));
    }

    @Test
    void testValidation_InvalidTemperature() {

        // NaN and infinity are invalid.
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Quantity<>(
                                Double.NaN, TemperatureUnit.CELSIUS)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Quantity<>(
                                Double.POSITIVE_INFINITY,
                                TemperatureUnit.FAHRENHEIT))
        );
    }
}
