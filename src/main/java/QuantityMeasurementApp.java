
public class QuantityMeasurementApp {

    // UC1: Keep Feet class for old test cases
    public static final class Feet {

        private final QuantityLength quantity;

        public Feet(double value) {
            this.quantity = new QuantityLength(
                    value, LengthUnit.FEET);
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Feet)) {
                return false;
            }

            Feet other = (Feet) obj;

            return this.quantity.equals(other.quantity);
        }

        @Override
        public int hashCode() {
            return quantity.hashCode();
        }
    }

    // UC2: Keep Inches class for old test cases
    public static final class Inches {

        private final QuantityLength quantity;

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

            return this.quantity.equals(other.quantity);
        }

        @Override
        public int hashCode() {
            return quantity.hashCode();
        }
    }

    // UC2: Existing Feet comparison method
    public static boolean compareFeet(double value1, double value2) {

        Feet feet1 = new Feet(value1);
        Feet feet2 = new Feet(value2);

        return feet1.equals(feet2);
    }

    // UC2: Existing Inches comparison method
    public static boolean compareInches(double value1, double value2) {

        Inches inches1 = new Inches(value1);
        Inches inches2 = new Inches(value2);

        return inches1.equals(inches2);
    }

    // Optional alias if your older code uses compareInch()
    public static boolean compareInch(double value1, double value2) {
        return compareInches(value1, value2);
    }

    // UC3: Compare measurements with different units
    public static boolean compareLengths(
            double value1, LengthUnit unit1,
            double value2, LengthUnit unit2) {

        QuantityLength q1 = new QuantityLength(value1, unit1);
        QuantityLength q2 = new QuantityLength(value2, unit2);

        return q1.equals(q2);
    }


    public static void demonstrateLengthConversion(
            double value,
            LengthUnit sourceUnit,
            LengthUnit targetUnit) {

        double result = QuantityLength.convert(
                value,
                sourceUnit,
                targetUnit
        );

        System.out.println(
                value + " " + sourceUnit +
                        " = " + result + " " + targetUnit
        );
    }

    // Overloaded version
    public static void demonstrateLengthConversion(
            QuantityLength quantity,
            LengthUnit targetUnit) {

        QuantityLength result = quantity.convertTo(targetUnit);

        System.out.println("Converted quantity: " + result);
    }


    //Demonstrate Addition
    public static void demonstrateAddition(
            double value1, LengthUnit unit1,
            double value2, LengthUnit unit2) {

        QuantityLength q1 = new QuantityLength(value1, unit1);
        QuantityLength q2 = new QuantityLength(value2, unit2);

        QuantityLength result = q1.add(q2);

        System.out.println(
                q1 + " + " + q2 + " = " + result
        );
    }


    // One equality method for all measurement categories
    public static <U extends IMeasurable> void demonstrateEquality(
            Quantity<U> first,
            Quantity<U> second) {

        System.out.println(
                first + " equals " + second
                        + " = " + first.equals(second)
        );
    }

    // One conversion method for all categories
    public static <U extends IMeasurable> void demonstrateConversion(
            Quantity<U> quantity,
            U targetUnit) {

        System.out.println(
                quantity + " converted to " + targetUnit
                        + " = " + quantity.convertTo(targetUnit)
        );
    }

    // One addition method for all categories
    public static <U extends IMeasurable> void demonstrateAddition(
            Quantity<U> first,
            Quantity<U> second,
            U targetUnit) {

        System.out.println(
                first + " + " + second
                        + " = " + first.add(second, targetUnit)
        );
    }


    // UC12: Generic subtraction demonstration.
    //
    // Works with LengthUnit, WeightUnit and VolumeUnit
    // because all implement IMeasurable.
    public static <U extends IMeasurable> void demonstrateSubtraction(
            Quantity<U> first,
            Quantity<U> second,
            U targetUnit) {

        // Perform subtraction in the requested unit.
        Quantity<U> result =
                first.subtract(second, targetUnit);

        // Display the operation and result.
        System.out.println(
                first + " - " + second + " = " + result
        );
    }


    // UC12: Generic division demonstration.
    public static <U extends IMeasurable> void demonstrateDivision(
            Quantity<U> first,
            Quantity<U> second) {

        // Divide the measurements.
        double result = first.divide(second);

        // Display the dimensionless ratio.
        System.out.println(
                first + " / " + second + " = " + result
        );
    }





    public static void main(String[] args) {

        // ==========================================
        // UC1: Feet Measurement Equality
        // ==========================================

        System.out.println("\n===== UC1: FEET EQUALITY =====");

        System.out.println("Same Feet: "
                + compareFeet(1.0, 1.0));

        System.out.println("Different Feet: "
                + compareFeet(1.0, 2.0));


        // ==========================================
        // UC2: Feet and Inches Equality
        // ==========================================

        System.out.println("\n===== UC2: INCHES EQUALITY =====");

        System.out.println("Same Inches: "
                + compareInches(1.0, 1.0));

        System.out.println("Different Inches: "
                + compareInches(1.0, 2.0));


        // ==========================================
        // UC3: Generic Length Equality
        // ==========================================

        System.out.println("\n===== UC3: LENGTH EQUALITY =====");

        System.out.println("1 Feet vs 12 Inches: "
                + compareLengths(
                1.0, LengthUnit.FEET,
                12.0, LengthUnit.INCHES));

        System.out.println("2 Feet vs 12 Inches: "
                + compareLengths(
                2.0, LengthUnit.FEET,
                12.0, LengthUnit.INCHES));


        // ==========================================
        // UC4: Extended Length Units
        // ==========================================

        System.out.println("\n===== UC4: EXTENDED LENGTH UNITS =====");

        System.out.println("1 Yard vs 3 Feet: "
                + compareLengths(
                1.0, LengthUnit.YARDS,
                3.0, LengthUnit.FEET));

        System.out.println("1 Yard vs 36 Inches: "
                + compareLengths(
                1.0, LengthUnit.YARDS,
                36.0, LengthUnit.INCHES));

        System.out.println("1 CM vs 0.393701 Inches: "
                + compareLengths(
                1.0, LengthUnit.CENTIMETERS,
                0.393701, LengthUnit.INCHES));

        System.out.println("2 CM vs 2 CM: "
                + compareLengths(
                2.0, LengthUnit.CENTIMETERS,
                2.0, LengthUnit.CENTIMETERS));

        System.out.println("1 Yard vs 2 Feet: "
                + compareLengths(
                1.0, LengthUnit.YARDS,
                2.0, LengthUnit.FEET));


        // ==========================================
        // UC5: Unit Conversion
        // ==========================================

        System.out.println("\n===== UC5: LENGTH CONVERSION =====");

        demonstrateLengthConversion(
                1.0,
                LengthUnit.FEET,
                LengthUnit.INCHES);

        demonstrateLengthConversion(
                3.0,
                LengthUnit.YARDS,
                LengthUnit.FEET);

        demonstrateLengthConversion(
                36.0,
                LengthUnit.INCHES,
                LengthUnit.YARDS);

        demonstrateLengthConversion(
                1.0,
                LengthUnit.CENTIMETERS,
                LengthUnit.INCHES);

        demonstrateLengthConversion(
                0.0,
                LengthUnit.FEET,
                LengthUnit.INCHES);

        QuantityLength uc5Length =
                new QuantityLength(2.0, LengthUnit.YARDS);

        demonstrateLengthConversion(
                uc5Length,
                LengthUnit.FEET);


        // ==========================================
        // UC6: Addition of Length Measurements
        // Result uses first operand's unit
        // ==========================================

        System.out.println("\n===== UC6: LENGTH ADDITION =====");

        QuantityLength uc6Feet1 =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength uc6Feet2 =
                new QuantityLength(2.0, LengthUnit.FEET);

        System.out.println("Feet + Feet: "
                + uc6Feet1.add(uc6Feet2));

        QuantityLength uc6Feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength uc6Inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

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
                new QuantityLength(2.54, LengthUnit.CENTIMETERS);

        QuantityLength uc6OneInch =
                new QuantityLength(1.0, LengthUnit.INCHES);

        System.out.println("CM + Inches: "
                + uc6Cm.add(uc6OneInch));

        QuantityLength uc6FiveFeet =
                new QuantityLength(5.0, LengthUnit.FEET);

        QuantityLength uc6NegativeFeet =
                new QuantityLength(-2.0, LengthUnit.FEET);

        System.out.println("Negative Addition: "
                + uc6FiveFeet.add(uc6NegativeFeet));

        // Existing demonstration method
        demonstrateAddition(
                1.0, LengthUnit.FEET,
                12.0, LengthUnit.INCHES);


        // ==========================================
        // UC7: Addition with Target Unit
        // ==========================================

        System.out.println("\n===== UC7: TARGET UNIT ADDITION =====");

        QuantityLength uc7Feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength uc7Inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println("Target FEET: "
                + uc7Feet.add(
                uc7Inches, LengthUnit.FEET));

        System.out.println("Target INCHES: "
                + uc7Feet.add(
                uc7Inches, LengthUnit.INCHES));

        System.out.println("Target YARDS: "
                + uc7Feet.add(
                uc7Inches, LengthUnit.YARDS));

        System.out.println("Target CENTIMETERS: "
                + uc7Feet.add(
                uc7Inches, LengthUnit.CENTIMETERS));

        System.out.println("Static Addition: "
                + QuantityLength.add(
                uc7Feet,
                uc7Inches,
                LengthUnit.YARDS));


        // ==========================================
        // UC8: Standalone LengthUnit Enum
        // ==========================================

        System.out.println("\n===== UC8: ENUM CONVERSION =====");

        System.out.println("12 Inches to Feet: "
                + LengthUnit.INCHES
                .convertToBaseUnit(12.0));

        System.out.println("1 Yard to Feet: "
                + LengthUnit.YARDS
                .convertToBaseUnit(1.0));

        System.out.println("2 Feet to Inches: "
                + LengthUnit.INCHES
                .convertFromBaseUnit(2.0));

        System.out.println("1 Foot to Centimeters: "
                + LengthUnit.CENTIMETERS
                .convertFromBaseUnit(1.0));

        QuantityLength uc8Feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength uc8Inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println("Equality after refactoring: "
                + uc8Feet.equals(uc8Inches));

        System.out.println("Conversion after refactoring: "
                + uc8Feet.convertTo(LengthUnit.INCHES));

        System.out.println("Addition after refactoring: "
                + uc8Feet.add(
                uc8Inches, LengthUnit.FEET));


        // ==========================================
        // UC9: Weight Measurement
        // ==========================================

        System.out.println("\n===== UC9: WEIGHT MEASUREMENT =====");

        QuantityWeight uc9Kg =
                new QuantityWeight(
                        1.0, WeightUnit.KILOGRAM);

        QuantityWeight uc9Grams =
                new QuantityWeight(
                        1000.0, WeightUnit.GRAM);

        QuantityWeight uc9Pounds =
                new QuantityWeight(
                        2.20462, WeightUnit.POUND);

        // Equality
        System.out.println("KG vs Grams: "
                + uc9Kg.equals(uc9Grams));

        System.out.println("KG vs Pounds: "
                + uc9Kg.equals(uc9Pounds));

        // Conversion
        System.out.println("KG to Grams: "
                + uc9Kg.convertTo(WeightUnit.GRAM));

        System.out.println("2 Pounds to KG: "
                + new QuantityWeight(
                2.0, WeightUnit.POUND)
                .convertTo(WeightUnit.KILOGRAM));

        // Addition
        System.out.println("KG + Grams: "
                + uc9Kg.add(uc9Grams));

        System.out.println("KG + Grams in GRAM: "
                + uc9Kg.add(
                uc9Grams, WeightUnit.GRAM));

        // Cross-category comparison
        QuantityLength uc9Length =
                new QuantityLength(
                        1.0, LengthUnit.FEET);

        System.out.println("Weight vs Length: "
                + uc9Kg.equals(uc9Length));


        // ==========================================
        // UC10: Generic Quantity with Interface
        // ==========================================

        System.out.println("\n===== UC10: GENERIC QUANTITY =====");

        // Generic Length objects
        Quantity<LengthUnit> uc10Feet =
                new Quantity<>(
                        1.0, LengthUnit.FEET);

        Quantity<LengthUnit> uc10Inches =
                new Quantity<>(
                        12.0, LengthUnit.INCHES);

        // Generic Weight objects
        Quantity<WeightUnit> uc10Kg =
                new Quantity<>(
                        1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> uc10Grams =
                new Quantity<>(
                        1000.0, WeightUnit.GRAM);

        // Generic length operations
        demonstrateEquality(
                uc10Feet, uc10Inches);

        demonstrateConversion(
                uc10Feet, LengthUnit.INCHES);

        demonstrateAddition(
                uc10Feet,
                uc10Inches,
                LengthUnit.FEET);

        // Generic weight operations
        demonstrateEquality(
                uc10Kg, uc10Grams);

        demonstrateConversion(
                uc10Kg, WeightUnit.GRAM);

        demonstrateAddition(
                uc10Kg,
                uc10Grams,
                WeightUnit.KILOGRAM);

        // Category incompatibility
        System.out.println("Length vs Weight: "
                + uc10Feet.equals(uc10Kg));


        // ==========================================
        // END
        // ==========================================

        System.out.println(
                "\n===== ALL USE CASES COMPLETED =====");

        // UC9: Weight Measurement

        // Equality
        QuantityWeight uc9DemoKg =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight uc9DemoGrams =
                new QuantityWeight(1000.0, WeightUnit.GRAM);

        System.out.println("KG vs Grams: "
                + uc9DemoKg.equals(uc9DemoGrams));

        // Pounds equality
                QuantityWeight uc9Pounds1 =
                        new QuantityWeight(2.20462, WeightUnit.POUND);

                System.out.println("KG vs Pounds: "
                        + uc9Kg.equals(uc9Pounds));

        // Conversion
                System.out.println("KG to Grams: "
                        + uc9Kg.convertTo(WeightUnit.GRAM));

                System.out.println("Pounds to KG: "
                        + new QuantityWeight(2.0, WeightUnit.POUND)
                        .convertTo(WeightUnit.KILOGRAM));

        // Addition - default target unit
                System.out.println("KG + Grams: "
                        + uc9Kg.add(uc9Grams));

        // Addition - explicit target unit
                System.out.println("KG + Grams in GRAM: "
                        + uc9Kg.add(uc9Grams, WeightUnit.GRAM));

        // Check category incompatibility
                QuantityLength uc9Length1 =
                        new QuantityLength(1.0, LengthUnit.FEET);

                System.out.println("Weight vs Length: "
                        + uc9Kg.equals(uc9Length));


        // ================================================
        // UC11: Volume Equality, Conversion and Addition
        // ================================================

                System.out.println("\n===== UC11: VOLUME MEASUREMENT =====");

        // Create a quantity representing 1 litre.
                Quantity<VolumeUnit> uc11Litre =
                        new Quantity<>(1.0, VolumeUnit.LITRE);

        // Create a quantity representing 1000 millilitres.
                Quantity<VolumeUnit> uc11Millilitres =
                        new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        // Create a quantity representing 1 US gallon.
                Quantity<VolumeUnit> uc11Gallon =
                        new Quantity<>(1.0, VolumeUnit.GALLON);


        // --------------------------------
        // 1. Equality Comparison
        // --------------------------------

        // Compare 1 litre with 1000 millilitres.
        // Expected: true
                System.out.println("Litre vs Millilitre: "
                        + uc11Litre.equals(uc11Millilitres));

        // Compare 1 gallon with 3.78541 litres.
        // Expected: true
                Quantity<VolumeUnit> uc11EquivalentLitres =
                        new Quantity<>(3.78541, VolumeUnit.LITRE);

                System.out.println("Gallon vs Litres: "
                        + uc11Gallon.equals(uc11EquivalentLitres));

        // Reuse generic demonstration method from UC10.
                demonstrateEquality(
                        uc11Litre,
                        uc11Millilitres
                );


        // --------------------------------
        // 2. Volume Conversion
        // --------------------------------

        // Convert 1 litre into millilitres.
        // Expected: 1000.0 MILLILITRE
                System.out.println("Litre to Millilitre: "
                        + uc11Litre.convertTo(VolumeUnit.MILLILITRE));

        // Convert 1 gallon into litres.
        // Expected: 3.78541 LITRE
                System.out.println("Gallon to Litre: "
                        + uc11Gallon.convertTo(VolumeUnit.LITRE));

        // Convert 1000 millilitres into gallons.
        // Expected: approximately 0.264172 GALLON
                System.out.println("Millilitre to Gallon: "
                        + uc11Millilitres.convertTo(VolumeUnit.GALLON));

        // Reuse UC10 generic conversion method.
                demonstrateConversion(
                        uc11Litre,
                        VolumeUnit.MILLILITRE
                );


        // --------------------------------
        // 3. Volume Addition
        // --------------------------------

        // Add 1 litre and 1000 millilitres.
        // Result is in the first operand's unit: LITRE.
        // Expected: 2.0 LITRE
                System.out.println("Litre + Millilitre: "
                        + uc11Litre.add(uc11Millilitres));

        // Add the same values but request MILLILITRE.
        // Expected: 2000.0 MILLILITRE
                System.out.println("Addition in Millilitres: "
                        + uc11Litre.add(
                        uc11Millilitres,
                        VolumeUnit.MILLILITRE
                ));

        // Add 1 gallon and 1 litre.
        // Result is expressed in GALLON.
                System.out.println("Gallon + Litre: "
                        + uc11Gallon.add(uc11Litre));

        // Reuse UC10 generic addition method.
                demonstrateAddition(
                        uc11Litre,
                        uc11Millilitres,
                        VolumeUnit.LITRE
                );


        // --------------------------------
        // 4. Cross-category Comparison
        // --------------------------------

        // A length cannot equal a volume.
                Quantity<LengthUnit> uc11Length =
                        new Quantity<>(1.0, LengthUnit.FEET);

        // A weight cannot equal a volume.
                Quantity<WeightUnit> uc11Weight =
                        new Quantity<>(1.0, WeightUnit.KILOGRAM);

        // Both comparisons should return false.
                System.out.println("Volume vs Length: "
                        + uc11Litre.equals(uc11Length));

                System.out.println("Volume vs Weight: "
                        + uc11Litre.equals(uc11Weight));



        // ==========================================
        // UC12: Subtraction and Division
        // ==========================================

                System.out.println("\n===== UC12: SUBTRACTION =====");

        // Create length quantities.
                Quantity<LengthUnit> uc12Feet =
                        new Quantity<>(10.0, LengthUnit.FEET);

                Quantity<LengthUnit> uc12Inches =
                        new Quantity<>(6.0, LengthUnit.INCHES);

        // Subtraction using first object's unit.
        // 10 feet - 6 inches = 9.5 feet.
                System.out.println("Length subtraction: "
                        + uc12Feet.subtract(uc12Inches));

        // Subtraction with explicitly requested unit.
        // 9.5 feet = 114 inches.
                System.out.println("Subtraction in inches: "
                        + uc12Feet.subtract(
                        uc12Inches,
                        LengthUnit.INCHES
                ));


        // Create weight quantities.
                Quantity<WeightUnit> uc12Kg =
                        new Quantity<>(10.0, WeightUnit.KILOGRAM);

                Quantity<WeightUnit> uc12Grams =
                        new Quantity<>(5000.0, WeightUnit.GRAM);

        // 10 kg - 5000 g = 5 kg.
                System.out.println("Weight subtraction: "
                        + uc12Kg.subtract(uc12Grams));


        // Create volume quantities.
                Quantity<VolumeUnit> uc12Litres =
                        new Quantity<>(5.0, VolumeUnit.LITRE);

                Quantity<VolumeUnit> uc12Ml =
                        new Quantity<>(500.0, VolumeUnit.MILLILITRE);

        // 5 litres - 500 millilitres = 4.5 litres.
                System.out.println("Volume subtraction: "
                        + uc12Litres.subtract(uc12Ml));


        // Reuse the generic demonstration method.
                demonstrateSubtraction(
                        uc12Feet,
                        uc12Inches,
                        LengthUnit.FEET
                );


        // ==========================================
        // UC12: Division
        // ==========================================

                System.out.println("\n===== UC12: DIVISION =====");

        // 10 feet / 6 inches = 20.
        // Because 6 inches = 0.5 feet.
                System.out.println("Length division: "
                        + uc12Feet.divide(uc12Inches));

        // 10 kg / 5000 g = 2.
                System.out.println("Weight division: "
                        + uc12Kg.divide(uc12Grams));

        // 5 litres / 500 millilitres = 10.
                System.out.println("Volume division: "
                        + uc12Litres.divide(uc12Ml));


        // Division with equivalent lengths.
                Quantity<LengthUnit> uc12TwoFeet =
                        new Quantity<>(2.0, LengthUnit.FEET);

                Quantity<LengthUnit> uc12TwentyFourInches =
                        new Quantity<>(24.0, LengthUnit.INCHES);

        // Both represent the same length,
        // so the ratio is 1.0.
                System.out.println("Equivalent length ratio: "
                        + uc12TwentyFourInches.divide(uc12TwoFeet));


        // Reuse the generic division demonstration.
                demonstrateDivision(
                        uc12Kg,
                        uc12Grams
                );

    }

}
