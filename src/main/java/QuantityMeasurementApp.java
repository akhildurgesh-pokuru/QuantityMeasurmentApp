
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


<<<<<<< HEAD
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
=======
        // UC9: Weight Measurement

        // Equality
                QuantityWeight uc9Kg =
                        new QuantityWeight(1.0, WeightUnit.KILOGRAM);

                QuantityWeight uc9Grams =
                        new QuantityWeight(1000.0, WeightUnit.GRAM);

                System.out.println("KG vs Grams: "
                        + uc9Kg.equals(uc9Grams));

        // Pounds equality
                QuantityWeight uc9Pounds =
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
                QuantityLength uc9Length =
                        new QuantityLength(1.0, LengthUnit.FEET);

                System.out.println("Weight vs Length: "
                        + uc9Kg.equals(uc9Length));

>>>>>>> 289727af6fa76c2289f449068d207e656fc261ee
    }

}
