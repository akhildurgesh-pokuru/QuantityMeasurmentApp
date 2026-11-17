
public class QuantityMeasurementApp {

    // =====================================================
    // UC1: Feet Measurement Equality
    // =====================================================

    // Keep this class for compatibility with old UC1 tests.
    public static final class Feet {

        private final QuantityLength quantity;

        // Constructor
        public Feet(double value) {
            this.quantity = new QuantityLength(
                    value, LengthUnit.FEET);
        }

        // Compare two Feet objects.
        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Feet)) {
                return false;
            }

            Feet other = (Feet) obj;

            return quantity.equals(other.quantity);
        }

        @Override
        public int hashCode() {
            return quantity.hashCode();
        }
    }


    // =====================================================
    // UC2: Inches Measurement Equality
    // =====================================================

    // Keep this class for compatibility with old UC2 tests.
    public static final class Inches {

        private final QuantityLength quantity;

        // Constructor
        public Inches(double value) {
            this.quantity = new QuantityLength(
                    value, LengthUnit.INCHES);
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Inches)) {
                return false;
            }

            Inches other = (Inches) obj;

            return quantity.equals(other.quantity);
        }

        @Override
        public int hashCode() {
            return quantity.hashCode();
        }
    }


    // =====================================================
    // UC2: Comparison Methods
    // =====================================================

    // Compare two values expressed in Feet.
    public static boolean compareFeet(
            double value1,
            double value2) {

        Feet feet1 = new Feet(value1);
        Feet feet2 = new Feet(value2);

        return feet1.equals(feet2);
    }

    // Compare two values expressed in Inches.
    public static boolean compareInches(
            double value1,
            double value2) {

        Inches inches1 = new Inches(value1);
        Inches inches2 = new Inches(value2);

        return inches1.equals(inches2);
    }

    // Older tests may use this method name.
    public static boolean compareInch(
            double value1,
            double value2) {

        return compareInches(value1, value2);
    }


    // =====================================================
    // UC3: Length Comparison Across Units
    // =====================================================

    public static boolean compareLengths(
            double value1,
            LengthUnit unit1,
            double value2,
            LengthUnit unit2) {

        // Create two length measurements.
        QuantityLength q1 =
                new QuantityLength(value1, unit1);

        QuantityLength q2 =
                new QuantityLength(value2, unit2);

        // Compare their normalized values.
        return q1.equals(q2);
    }


    // =====================================================
    // UC5: Length Conversion Demonstration
    // =====================================================

    public static void demonstrateLengthConversion(
            double value,
            LengthUnit sourceUnit,
            LengthUnit targetUnit) {

        // Convert the input to the target unit.
        double result = QuantityLength.convert(
                value,
                sourceUnit,
                targetUnit
        );

        // Print the conversion result.
        System.out.println(
                value + " " + sourceUnit
                        + " = " + result + " " + targetUnit
        );
    }

    // Overloaded method accepting an existing object.
    public static void demonstrateLengthConversion(
            QuantityLength quantity,
            LengthUnit targetUnit) {

        QuantityLength result =
                quantity.convertTo(targetUnit);

        System.out.println("Converted quantity: " + result);
    }


    // =====================================================
    // UC6: Length Addition Demonstration
    // =====================================================

    public static void demonstrateAddition(
            double value1,
            LengthUnit unit1,
            double value2,
            LengthUnit unit2) {

        QuantityLength q1 =
                new QuantityLength(value1, unit1);

        QuantityLength q2 =
                new QuantityLength(value2, unit2);

        // Add the measurements.
        QuantityLength result = q1.add(q2);

        System.out.println(
                q1 + " + " + q2 + " = " + result
        );
    }


    // =====================================================
    // UC10: Generic Equality Demonstration
    // =====================================================

    // Works for Length, Weight, Volume and Temperature.
    public static <U extends IMeasurable> void demonstrateEquality(
            Quantity<U> first,
            Quantity<U> second) {

        System.out.println(
                first + " equals " + second
                        + " = " + first.equals(second)
        );
    }


    // =====================================================
    // UC10: Generic Conversion Demonstration
    // =====================================================

    public static <U extends IMeasurable> void demonstrateConversion(
            Quantity<U> quantity,
            U targetUnit) {

        System.out.println(
                quantity + " converted to " + targetUnit
                        + " = " + quantity.convertTo(targetUnit)
        );
    }


    // =====================================================
    // UC10: Generic Addition Demonstration
    // =====================================================

    public static <U extends IMeasurable> void demonstrateAddition(
            Quantity<U> first,
            Quantity<U> second,
            U targetUnit) {

        System.out.println(
                first + " + " + second
                        + " = " + first.add(second, targetUnit)
        );
    }


    // =====================================================
    // UC12: Generic Subtraction Demonstration
    // =====================================================

    public static <U extends IMeasurable> void demonstrateSubtraction(
            Quantity<U> first,
            Quantity<U> second,
            U targetUnit) {

        // Subtract the second quantity from the first.
        Quantity<U> result =
                first.subtract(second, targetUnit);

        System.out.println(
                first + " - " + second + " = " + result
        );
    }


    // =====================================================
    // UC12: Generic Division Demonstration
    // =====================================================

    public static <U extends IMeasurable> void demonstrateDivision(
            Quantity<U> first,
            Quantity<U> second) {

        // Division returns a dimensionless ratio.
        double result = first.divide(second);

        System.out.println(
                first + " / " + second + " = " + result
        );
    }


    // =====================================================
    // MAIN METHOD: UC1 TO UC14
    // =====================================================

    public static void main(String[] args) {


        // =================================================
        // UC1: Feet Equality
        // =================================================

        System.out.println("\n===== UC1: FEET EQUALITY =====");

        System.out.println("Same Feet: "
                + compareFeet(1.0, 1.0));

        System.out.println("Different Feet: "
                + compareFeet(1.0, 2.0));


        // =================================================
        // UC2: Inches Equality
        // =================================================

        System.out.println("\n===== UC2: INCHES EQUALITY =====");

        System.out.println("Same Inches: "
                + compareInches(1.0, 1.0));

        System.out.println("Different Inches: "
                + compareInches(1.0, 2.0));


        // =================================================
        // UC3: Cross-Unit Length Equality
        // =================================================

        System.out.println("\n===== UC3: LENGTH EQUALITY =====");

        // 1 foot equals 12 inches.
        System.out.println("1 Foot vs 12 Inches: "
                + compareLengths(
                1.0, LengthUnit.FEET,
                12.0, LengthUnit.INCHES
        ));

        // 2 feet does not equal 12 inches.
        System.out.println("2 Feet vs 12 Inches: "
                + compareLengths(
                2.0, LengthUnit.FEET,
                12.0, LengthUnit.INCHES
        ));


        // =================================================
        // UC4: Extended Length Units
        // =================================================

        System.out.println(
                "\n===== UC4: EXTENDED LENGTH UNITS =====");

        // 1 yard equals 3 feet.
        System.out.println("1 Yard vs 3 Feet: "
                + compareLengths(
                1.0, LengthUnit.YARDS,
                3.0, LengthUnit.FEET
        ));

        // 1 yard equals 36 inches.
        System.out.println("1 Yard vs 36 Inches: "
                + compareLengths(
                1.0, LengthUnit.YARDS,
                36.0, LengthUnit.INCHES
        ));

        // Compare centimeter and inch values.
        System.out.println("1 CM vs 0.393701 Inches: "
                + compareLengths(
                1.0, LengthUnit.CENTIMETERS,
                0.393701, LengthUnit.INCHES
        ));

        // Same centimeter values.
        System.out.println("2 CM vs 2 CM: "
                + compareLengths(
                2.0, LengthUnit.CENTIMETERS,
                2.0, LengthUnit.CENTIMETERS
        ));

        // Different measurements should not be equal.
        System.out.println("1 Yard vs 2 Feet: "
                + compareLengths(
                1.0, LengthUnit.YARDS,
                2.0, LengthUnit.FEET
        ));


        // =================================================
        // UC5: Length Conversion
        // =================================================

        System.out.println(
                "\n===== UC5: LENGTH CONVERSION =====");

        demonstrateLengthConversion(
                1.0,
                LengthUnit.FEET,
                LengthUnit.INCHES
        );

        demonstrateLengthConversion(
                3.0,
                LengthUnit.YARDS,
                LengthUnit.FEET
        );

        demonstrateLengthConversion(
                36.0,
                LengthUnit.INCHES,
                LengthUnit.YARDS
        );

        demonstrateLengthConversion(
                1.0,
                LengthUnit.CENTIMETERS,
                LengthUnit.INCHES
        );

        demonstrateLengthConversion(
                0.0,
                LengthUnit.FEET,
                LengthUnit.INCHES
        );

        // Convert an existing length object.
        QuantityLength uc5Length =
                new QuantityLength(2.0, LengthUnit.YARDS);

        demonstrateLengthConversion(
                uc5Length,
                LengthUnit.FEET
        );


        // =================================================
        // UC6: Addition of Length Measurements
        // =================================================

        System.out.println(
                "\n===== UC6: LENGTH ADDITION =====");

        QuantityLength uc6Feet1 =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength uc6Feet2 =
                new QuantityLength(2.0, LengthUnit.FEET);

        // 1 foot + 2 feet = 3 feet.
        System.out.println("Feet + Feet: "
                + uc6Feet1.add(uc6Feet2));


        QuantityLength uc6Feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength uc6Inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        // Result uses first object's unit.
        System.out.println("Feet + Inches: "
                + uc6Feet.add(uc6Inches));

        System.out.println("Inches + Feet: "
                + uc6Inches.add(uc6Feet));


        QuantityLength uc6Yards =
                new QuantityLength(1.0, LengthUnit.YARDS);

        QuantityLength uc6ThreeFeet =
                new QuantityLength(3.0, LengthUnit.FEET);

        System.out.println("Yards + Feet: "
                + uc6Yards.add(uc6ThreeFeet));


        QuantityLength uc6Cm =
                new QuantityLength(
                        2.54, LengthUnit.CENTIMETERS);

        QuantityLength uc6OneInch =
                new QuantityLength(
                        1.0, LengthUnit.INCHES);

        System.out.println("CM + Inches: "
                + uc6Cm.add(uc6OneInch));


        QuantityLength uc6FiveFeet =
                new QuantityLength(5.0, LengthUnit.FEET);

        QuantityLength uc6NegativeFeet =
                new QuantityLength(-2.0, LengthUnit.FEET);

        System.out.println("Negative Addition: "
                + uc6FiveFeet.add(uc6NegativeFeet));

        // Reuse existing addition demonstration.
        demonstrateAddition(
                1.0, LengthUnit.FEET,
                12.0, LengthUnit.INCHES
        );


        // =================================================
        // UC7: Addition With Target Unit
        // =================================================

        System.out.println(
                "\n===== UC7: TARGET UNIT ADDITION =====");

        QuantityLength uc7Feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength uc7Inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        // Add and express the result in different units.
        System.out.println("Target FEET: "
                + uc7Feet.add(
                uc7Inches, LengthUnit.FEET
        ));

        System.out.println("Target INCHES: "
                + uc7Feet.add(
                uc7Inches, LengthUnit.INCHES
        ));

        System.out.println("Target YARDS: "
                + uc7Feet.add(
                uc7Inches, LengthUnit.YARDS
        ));

        System.out.println("Target CENTIMETERS: "
                + uc7Feet.add(
                uc7Inches, LengthUnit.CENTIMETERS
        ));

        // Static addition.
        System.out.println("Static Addition: "
                + QuantityLength.add(
                uc7Feet,
                uc7Inches,
                LengthUnit.YARDS
        ));


        // =================================================
        // UC8: LengthUnit Enum Conversion
        // =================================================

        System.out.println(
                "\n===== UC8: ENUM CONVERSION =====");

        // Convert 12 inches into feet.
        System.out.println("12 Inches to Feet: "
                + LengthUnit.INCHES
                .convertToBaseUnit(12.0));

        // Convert 1 yard into feet.
        System.out.println("1 Yard to Feet: "
                + LengthUnit.YARDS
                .convertToBaseUnit(1.0));

        // Convert 2 feet into inches.
        System.out.println("2 Feet to Inches: "
                + LengthUnit.INCHES
                .convertFromBaseUnit(2.0));

        // Convert 1 foot into centimeters.
        System.out.println("1 Foot to Centimeters: "
                + LengthUnit.CENTIMETERS
                .convertFromBaseUnit(1.0));


        QuantityLength uc8Feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength uc8Inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        // Verify earlier functionality still works.
        System.out.println("Equality after refactoring: "
                + uc8Feet.equals(uc8Inches));

        System.out.println("Conversion after refactoring: "
                + uc8Feet.convertTo(LengthUnit.INCHES));

        System.out.println("Addition after refactoring: "
                + uc8Feet.add(
                uc8Inches, LengthUnit.FEET
        ));


        // =================================================
        // UC9: Weight Measurement
        // =================================================

        System.out.println(
                "\n===== UC9: WEIGHT MEASUREMENT =====");

        QuantityWeight uc9Kg =
                new QuantityWeight(
                        1.0, WeightUnit.KILOGRAM);

        QuantityWeight uc9Grams =
                new QuantityWeight(
                        1000.0, WeightUnit.GRAM);

        QuantityWeight uc9Pounds =
                new QuantityWeight(
                        2.20462, WeightUnit.POUND);

        // Equality across different weight units.
        System.out.println("KG vs Grams: "
                + uc9Kg.equals(uc9Grams));

        System.out.println("KG vs Pounds: "
                + uc9Kg.equals(uc9Pounds));

        // Conversion from kilograms to grams.
        System.out.println("KG to Grams: "
                + uc9Kg.convertTo(WeightUnit.GRAM));

        // Conversion from pounds to kilograms.
        System.out.println("2 Pounds to KG: "
                + new QuantityWeight(
                2.0, WeightUnit.POUND
        ).convertTo(WeightUnit.KILOGRAM));

        // Weight addition.
        System.out.println("KG + Grams: "
                + uc9Kg.add(uc9Grams));

        // Addition with explicit target unit.
        System.out.println("KG + Grams in GRAM: "
                + uc9Kg.add(
                uc9Grams, WeightUnit.GRAM
        ));

        // Weight and length belong to different categories.
        QuantityLength uc9Length =
                new QuantityLength(
                        1.0, LengthUnit.FEET);

        System.out.println("Weight vs Length: "
                + uc9Kg.equals(uc9Length));


        // =================================================
        // UC10: Generic Quantity Using IMeasurable
        // =================================================

        System.out.println(
                "\n===== UC10: GENERIC QUANTITY =====");

        // Generic length measurements.
        Quantity<LengthUnit> uc10Feet =
                new Quantity<>(
                        1.0, LengthUnit.FEET);

        Quantity<LengthUnit> uc10Inches =
                new Quantity<>(
                        12.0, LengthUnit.INCHES);

        // Generic weight measurements.
        Quantity<WeightUnit> uc10Kg =
                new Quantity<>(
                        1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> uc10Grams =
                new Quantity<>(
                        1000.0, WeightUnit.GRAM);

        // Generic length equality.
        demonstrateEquality(
                uc10Feet,
                uc10Inches
        );

        // Generic length conversion.
        demonstrateConversion(
                uc10Feet,
                LengthUnit.INCHES
        );

        // Generic length addition.
        demonstrateAddition(
                uc10Feet,
                uc10Inches,
                LengthUnit.FEET
        );

        // Generic weight equality.
        demonstrateEquality(
                uc10Kg,
                uc10Grams
        );

        // Generic weight conversion.
        demonstrateConversion(
                uc10Kg,
                WeightUnit.GRAM
        );

        // Generic weight addition.
        demonstrateAddition(
                uc10Kg,
                uc10Grams,
                WeightUnit.KILOGRAM
        );

        // Length and weight cannot be equal.
        System.out.println("Length vs Weight: "
                + uc10Feet.equals(uc10Kg));


        // =================================================
        // UC11: Volume Measurement
        // =================================================

        System.out.println(
                "\n===== UC11: VOLUME MEASUREMENT =====");

        // Create volume measurements.
        Quantity<VolumeUnit> uc11Litre =
                new Quantity<>(
                        1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> uc11Millilitres =
                new Quantity<>(
                        1000.0, VolumeUnit.MILLILITRE);

        Quantity<VolumeUnit> uc11Gallon =
                new Quantity<>(
                        1.0, VolumeUnit.GALLON);

        // 1 litre equals 1000 millilitres.
        System.out.println("Litre vs Millilitre: "
                + uc11Litre.equals(uc11Millilitres));

        // 1 gallon is approximately 3.78541 litres.
        Quantity<VolumeUnit> uc11EquivalentLitres =
                new Quantity<>(
                        3.78541, VolumeUnit.LITRE);

        System.out.println("Gallon vs Litres: "
                + uc11Gallon.equals(uc11EquivalentLitres));

        // Reuse UC10 generic equality method.
        demonstrateEquality(
                uc11Litre,
                uc11Millilitres
        );

        // Volume conversions.
        System.out.println("Litre to Millilitre: "
                + uc11Litre.convertTo(
                VolumeUnit.MILLILITRE));

        System.out.println("Gallon to Litre: "
                + uc11Gallon.convertTo(
                VolumeUnit.LITRE));

        System.out.println("Millilitre to Gallon: "
                + uc11Millilitres.convertTo(
                VolumeUnit.GALLON));

        demonstrateConversion(
                uc11Litre,
                VolumeUnit.MILLILITRE
        );

        // Addition in the first object's unit.
        System.out.println("Litre + Millilitre: "
                + uc11Litre.add(uc11Millilitres));

        // Addition with explicit target unit.
        System.out.println("Addition in Millilitres: "
                + uc11Litre.add(
                uc11Millilitres,
                VolumeUnit.MILLILITRE
        ));

        System.out.println("Gallon + Litre: "
                + uc11Gallon.add(uc11Litre));

        demonstrateAddition(
                uc11Litre,
                uc11Millilitres,
                VolumeUnit.LITRE
        );

        // Cross-category comparisons.
        Quantity<LengthUnit> uc11Length =
                new Quantity<>(
                        1.0, LengthUnit.FEET);

        Quantity<WeightUnit> uc11Weight =
                new Quantity<>(
                        1.0, WeightUnit.KILOGRAM);

        System.out.println("Volume vs Length: "
                + uc11Litre.equals(uc11Length));

        System.out.println("Volume vs Weight: "
                + uc11Litre.equals(uc11Weight));


        // =================================================
        // UC12: Subtraction and Division
        // =================================================

        System.out.println(
                "\n===== UC12: SUBTRACTION =====");

        Quantity<LengthUnit> uc12Feet =
                new Quantity<>(
                        10.0, LengthUnit.FEET);

        Quantity<LengthUnit> uc12Inches =
                new Quantity<>(
                        6.0, LengthUnit.INCHES);

        // 10 feet - 6 inches = 9.5 feet.
        System.out.println("Length subtraction: "
                + uc12Feet.subtract(uc12Inches));

        // 9.5 feet = 114 inches.
        System.out.println("Subtraction in inches: "
                + uc12Feet.subtract(
                uc12Inches,
                LengthUnit.INCHES
        ));

        // Weight subtraction.
        Quantity<WeightUnit> uc12Kg =
                new Quantity<>(
                        10.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> uc12Grams =
                new Quantity<>(
                        5000.0, WeightUnit.GRAM);

        // 10 kilograms - 5 kilograms = 5 kilograms.
        System.out.println("Weight subtraction: "
                + uc12Kg.subtract(uc12Grams));

        // Volume subtraction.
        Quantity<VolumeUnit> uc12Litres =
                new Quantity<>(
                        5.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> uc12Ml =
                new Quantity<>(
                        500.0, VolumeUnit.MILLILITRE);

        // 5 litres - 0.5 litres = 4.5 litres.
        System.out.println("Volume subtraction: "
                + uc12Litres.subtract(uc12Ml));

        // Generic subtraction demonstration.
        demonstrateSubtraction(
                uc12Feet,
                uc12Inches,
                LengthUnit.FEET
        );


        // =================================================
        // UC12: Division
        // =================================================

        System.out.println(
                "\n===== UC12: DIVISION =====");

        // Division returns a double, not a Quantity.
        System.out.println("Length division: "
                + uc12Feet.divide(uc12Inches));

        System.out.println("Weight division: "
                + uc12Kg.divide(uc12Grams));

        System.out.println("Volume division: "
                + uc12Litres.divide(uc12Ml));

        // Compare equivalent length measurements.
        Quantity<LengthUnit> uc12TwoFeet =
                new Quantity<>(
                        2.0, LengthUnit.FEET);

        Quantity<LengthUnit> uc12TwentyFourInches =
                new Quantity<>(
                        24.0, LengthUnit.INCHES);

        // 24 inches / 2 feet = 1.
        System.out.println("Equivalent length ratio: "
                + uc12TwentyFourInches.divide(
                uc12TwoFeet));

        demonstrateDivision(
                uc12Kg,
                uc12Grams
        );


        // =================================================
        // UC13: Centralized Arithmetic Logic
        // =================================================

        System.out.println(
                "\n===== UC13: CENTRALIZED ARITHMETIC =====");

        // Create two length quantities.
        Quantity<LengthUnit> uc13Feet =
                new Quantity<>(
                        10.0, LengthUnit.FEET);

        Quantity<LengthUnit> uc13Inches =
                new Quantity<>(
                        6.0, LengthUnit.INCHES);

        // Addition uses the centralized arithmetic helper.
        System.out.println("Addition: "
                + uc13Feet.add(uc13Inches));

        // Subtraction uses the same helper.
        System.out.println("Subtraction: "
                + uc13Feet.subtract(uc13Inches));

        // Division uses the same helper.
        System.out.println("Division: "
                + uc13Feet.divide(uc13Inches));

        // Explicit target unit.
        System.out.println("Subtraction in INCHES: "
                + uc13Feet.subtract(
                uc13Inches,
                LengthUnit.INCHES
        ));

        // Weight arithmetic.
        Quantity<WeightUnit> uc13Kg =
                new Quantity<>(
                        10.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> uc13Grams =
                new Quantity<>(
                        5000.0, WeightUnit.GRAM);

        System.out.println("Weight subtraction: "
                + uc13Kg.subtract(uc13Grams));

        // Volume arithmetic.
        Quantity<VolumeUnit> uc13Litres =
                new Quantity<>(
                        5.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> uc13Ml =
                new Quantity<>(
                        500.0, VolumeUnit.MILLILITRE);

        System.out.println("Volume subtraction: "
                + uc13Litres.subtract(uc13Ml));


        // =================================================
        // UC14: Temperature Measurement
        // =================================================

        System.out.println(
                "\n===== UC14: TEMPERATURE MEASUREMENT =====");


        // -----------------------------------------
        // UC14.1: Create Temperature Measurements
        // -----------------------------------------

        // 0 degrees Celsius.
        Quantity<TemperatureUnit> uc14Celsius =
                new Quantity<>(
                        0.0, TemperatureUnit.CELSIUS);

        // 32 degrees Fahrenheit.
        Quantity<TemperatureUnit> uc14Fahrenheit =
                new Quantity<>(
                        32.0, TemperatureUnit.FAHRENHEIT);

        // 273.15 Kelvin.
        Quantity<TemperatureUnit> uc14Kelvin =
                new Quantity<>(
                        273.15, TemperatureUnit.KELVIN);


        // -----------------------------------------
        // UC14.2: Temperature Equality
        // -----------------------------------------

        // 0 Celsius equals 32 Fahrenheit.
        System.out.println("Celsius vs Fahrenheit: "
                + uc14Celsius.equals(uc14Fahrenheit));

        // 0 Celsius equals 273.15 Kelvin.
        System.out.println("Celsius vs Kelvin: "
                + uc14Celsius.equals(uc14Kelvin));

        // 32 Fahrenheit equals 273.15 Kelvin.
        System.out.println("Fahrenheit vs Kelvin: "
                + uc14Fahrenheit.equals(uc14Kelvin));

        // Reuse the generic UC10 equality method.
        demonstrateEquality(
                uc14Celsius,
                uc14Fahrenheit
        );


        // -----------------------------------------
        // UC14.3: Temperature Conversion
        // -----------------------------------------

        // Create boiling-point temperature.
        Quantity<TemperatureUnit> uc14Boiling =
                new Quantity<>(
                        100.0, TemperatureUnit.CELSIUS);

        // 100 Celsius = 212 Fahrenheit.
        System.out.println("100 Celsius to Fahrenheit: "
                + uc14Boiling.convertTo(
                TemperatureUnit.FAHRENHEIT));

        // 32 Fahrenheit = 0 Celsius.
        System.out.println("32 Fahrenheit to Celsius: "
                + uc14Fahrenheit.convertTo(
                TemperatureUnit.CELSIUS));

        // 0 Celsius = 273.15 Kelvin.
        System.out.println("0 Celsius to Kelvin: "
                + uc14Celsius.convertTo(
                TemperatureUnit.KELVIN));

        // 273.15 Kelvin = 0 Celsius.
        System.out.println("273.15 Kelvin to Celsius: "
                + uc14Kelvin.convertTo(
                TemperatureUnit.CELSIUS));

        // Convert Fahrenheit into Kelvin.
        System.out.println("32 Fahrenheit to Kelvin: "
                + uc14Fahrenheit.convertTo(
                TemperatureUnit.KELVIN));

        // Reuse generic conversion method.
        demonstrateConversion(
                uc14Boiling,
                TemperatureUnit.FAHRENHEIT
        );


        // -----------------------------------------
        // UC14.4: Arithmetic Support Checks
        // -----------------------------------------

        // Temperature does not support absolute addition.
        System.out.println("Temperature Supports Addition: "
                + TemperatureUnit.CELSIUS
                .supportsAddition());

        // Temperature subtraction is unsupported in UC14.
        System.out.println("Temperature Supports Subtraction: "
                + TemperatureUnit.CELSIUS
                .supportsSubtraction());

        // Temperature division is unsupported.
        System.out.println("Temperature Supports Division: "
                + TemperatureUnit.FAHRENHEIT
                .supportsDivision());

        // Existing measurement categories still
        // support their normal arithmetic operations.
        System.out.println("Length Supports Addition: "
                + LengthUnit.FEET.supportsAddition());

        System.out.println("Weight Supports Division: "
                + WeightUnit.KILOGRAM.supportsDivision());

        System.out.println("Volume Supports Subtraction: "
                + VolumeUnit.LITRE.supportsSubtraction());


        // -----------------------------------------
        // UC14.5: Unsupported Addition
        // -----------------------------------------

        Quantity<TemperatureUnit> uc14Fifty =
                new Quantity<>(
                        50.0, TemperatureUnit.CELSIUS);

        // The arithmetic support check in Quantity
        // throws UnsupportedOperationException.
        //
        // Catching it allows main() to continue.
        try {

            uc14Boiling.add(uc14Fifty);

        } catch (UnsupportedOperationException e) {

            System.out.println(
                    "Temperature Addition Error: "
                            + e.getMessage()
            );
        }


        // -----------------------------------------
        // UC14.6: Unsupported Subtraction
        // -----------------------------------------

        try {

            uc14Boiling.subtract(uc14Fifty);

        } catch (UnsupportedOperationException e) {

            System.out.println(
                    "Temperature Subtraction Error: "
                            + e.getMessage()
            );
        }


        // -----------------------------------------
        // UC14.7: Unsupported Division
        // -----------------------------------------

        try {

            uc14Boiling.divide(uc14Fifty);

        } catch (UnsupportedOperationException e) {

            System.out.println(
                    "Temperature Division Error: "
                            + e.getMessage()
            );
        }


        // -----------------------------------------
        // UC14.8: Cross-Category Comparison
        // -----------------------------------------

        // Create a length quantity.
        Quantity<LengthUnit> uc14Length =
                new Quantity<>(
                        100.0, LengthUnit.FEET);

        // Create a weight quantity.
        Quantity<WeightUnit> uc14Weight =
                new Quantity<>(
                        100.0, WeightUnit.KILOGRAM);

        // Create a volume quantity.
        Quantity<VolumeUnit> uc14Volume =
                new Quantity<>(
                        100.0, VolumeUnit.LITRE);

        // Temperature is not equal to length.
        System.out.println("Temperature vs Length: "
                + uc14Boiling.equals(uc14Length));

        // Temperature is not equal to weight.
        System.out.println("Temperature vs Weight: "
                + uc14Boiling.equals(uc14Weight));

        // Temperature is not equal to volume.
        System.out.println("Temperature vs Volume: "
                + uc14Boiling.equals(uc14Volume));


        // =================================================
        // ALL USE CASES COMPLETED
        // =================================================

        System.out.println(
                "\n===== ALL USE CASES UC1 TO UC14 COMPLETED =====");

    }
}
