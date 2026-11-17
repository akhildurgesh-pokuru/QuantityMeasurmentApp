
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

        // UC1: Feet to Feet
        System.out.println("Feet equality: "
                + compareFeet(1.0, 1.0));

        // UC2: Inches to Inches
        System.out.println("Inches equality: "
                + compareInches(1.0, 1.0));

        // UC3: Feet to Inches
        System.out.println("Feet to Inches: "
                + compareLengths(
                1.0, LengthUnit.FEET,
                12.0, LengthUnit.INCHES));

        // UC4: Yard to Feet
        System.out.println("Yard to Feet: "
                + compareLengths(
                1.0, LengthUnit.YARDS,
                3.0, LengthUnit.FEET));

        // UC4: Yard to Inches
        System.out.println("Yard to Inches: "
                + compareLengths(
                1.0, LengthUnit.YARDS,
                36.0, LengthUnit.INCHES));

        // UC4: Centimeters to Inches
        System.out.println("CM to Inches: "
                + compareLengths(
                1.0, LengthUnit.CENTIMETERS,
                0.393701, LengthUnit.INCHES));

        // UC4: Centimeters to Centimeters
        System.out.println("CM to CM: "
                + compareLengths(
                2.0, LengthUnit.CENTIMETERS,
                2.0, LengthUnit.CENTIMETERS));

        // UC4: Different measurements
        System.out.println("Different measurements: "
                + compareLengths(
                1.0, LengthUnit.YARDS,
                2.0, LengthUnit.FEET));


        // UC5: Feet to Inches
        demonstrateLengthConversion(
                1.0,
                LengthUnit.FEET,
                LengthUnit.INCHES
        );

        // UC5: Yards to Feet
        demonstrateLengthConversion(
                3.0,
                LengthUnit.YARDS,
                LengthUnit.FEET
        );

        // UC5: Inches to Yards
        demonstrateLengthConversion(
                36.0,
                LengthUnit.INCHES,
                LengthUnit.YARDS
        );

        // UC5: Centimeters to Inches
        demonstrateLengthConversion(
                1.0,
                LengthUnit.CENTIMETERS,
                LengthUnit.INCHES
        );

        // UC5: Zero
        demonstrateLengthConversion(
                0.0,
                LengthUnit.FEET,
                LengthUnit.INCHES
        );

        // UC5: Instance conversion
        QuantityLength length =
                new QuantityLength(2.0, LengthUnit.YARDS);

        demonstrateLengthConversion(
                length,
                LengthUnit.FEET
        );


        // UC6: Same-unit addition
        QuantityLength feet1 =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength feet2 =
                new QuantityLength(2.0, LengthUnit.FEET);

        System.out.println("Feet + Feet: " + feet1.add(feet2));


        // UC6: Feet + Inches
        QuantityLength feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println("Feet + Inches: " + feet.add(inches));


        // UC6: Inches + Feet
        System.out.println("Inches + Feet: " + inches.add(feet));


        // UC6: Yards + Feet
        QuantityLength yards =
                new QuantityLength(1.0, LengthUnit.YARDS);

        QuantityLength threeFeet =
                new QuantityLength(3.0, LengthUnit.FEET);

        System.out.println("Yards + Feet: " + yards.add(threeFeet));


        // UC6: Centimeters + Inches
        QuantityLength cm =
                new QuantityLength(2.54, LengthUnit.CENTIMETERS);

        QuantityLength oneInch =
                new QuantityLength(1.0, LengthUnit.INCHES);

        System.out.println("CM + Inches: " + cm.add(oneInch));


        // UC6: Negative values
        QuantityLength fiveFeet =
                new QuantityLength(5.0, LengthUnit.FEET);

        QuantityLength negativeFeet =
                new QuantityLength(-2.0, LengthUnit.FEET);

        System.out.println("Negative addition: "
                + fiveFeet.add(negativeFeet));


        // UC6 examples

        demonstrateAddition(
                1.0, LengthUnit.FEET,
                2.0, LengthUnit.FEET);

        demonstrateAddition(
                1.0, LengthUnit.FEET,
                12.0, LengthUnit.INCHES);

        demonstrateAddition(
                12.0, LengthUnit.INCHES,
                1.0, LengthUnit.FEET);

        demonstrateAddition(
                1.0, LengthUnit.YARDS,
                3.0, LengthUnit.FEET);

        demonstrateAddition(
                2.54, LengthUnit.CENTIMETERS,
                1.0, LengthUnit.INCHES);

        demonstrateAddition(
                5.0, LengthUnit.FEET,
                -2.0, LengthUnit.FEET);


        // UC7: Addition with explicit target unit

        QuantityLength UC7feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength UC7inches =
                new QuantityLength(12.0, LengthUnit.INCHES);


        // UC7: Addition with explicit target unit

        QuantityLength uc7Feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength uc7Inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        // Result in feet
        System.out.println("Target FEET: "
                + uc7Feet.add(uc7Inches, LengthUnit.FEET));

        // Result in inches
        System.out.println("Target INCHES: "
                + uc7Feet.add(uc7Inches, LengthUnit.INCHES));

        // Result in yards
        System.out.println("Target YARDS: "
                + uc7Feet.add(uc7Inches, LengthUnit.YARDS));

        // Result in centimeters
        System.out.println("Target CENTIMETERS: "
                + uc7Feet.add(uc7Inches, LengthUnit.CENTIMETERS));

        // Using the static addition method
        System.out.println("Static addition: "
                + QuantityLength.add(
                uc7Feet,
                uc7Inches,
                LengthUnit.YARDS));


        // UC8: Standalone enum conversions

        System.out.println("12 inches to feet: "
                + LengthUnit.INCHES.convertToBaseUnit(12.0));

        System.out.println("1 yard to feet: "
                + LengthUnit.YARDS.convertToBaseUnit(1.0));

        System.out.println("2 feet to inches: "
                + LengthUnit.INCHES.convertFromBaseUnit(2.0));

        System.out.println("1 foot to cm: "
                + LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0));

        // Existing QuantityLength APIs still work
        QuantityLength uc8Feet =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength uc8Inches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println("Equality: "
                + uc8Feet.equals(uc8Inches));

        System.out.println("Conversion: "
                + uc8Feet.convertTo(LengthUnit.INCHES));

        System.out.println("Addition: "
                + uc8Feet.add(uc8Inches, LengthUnit.FEET));




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



        // UC10: Generic Quantity demonstration

        Quantity<LengthUnit> uc10Feet =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> uc10Inches =
                new Quantity<>(12.0, LengthUnit.INCHES);

        // Length equality
                demonstrateEquality(uc10Feet, uc10Inches);

        // Length conversion
                demonstrateConversion(uc10Feet, LengthUnit.INCHES);

        // Length addition
                demonstrateAddition(
                        uc10Feet,
                        uc10Inches,
                        LengthUnit.FEET
                );

        // Weight objects
                Quantity<WeightUnit> uc10Kg =
                        new Quantity<>(1.0, WeightUnit.KILOGRAM);

                Quantity<WeightUnit> uc10Grams =
                        new Quantity<>(1000.0, WeightUnit.GRAM);

        // Weight equality
                demonstrateEquality(uc10Kg, uc10Grams);

        // Weight conversion
                demonstrateConversion(uc10Kg, WeightUnit.GRAM);

        // Weight addition
                demonstrateAddition(
                        uc10Kg,
                        uc10Grams,
                        WeightUnit.KILOGRAM
                );

        // Runtime category check
                System.out.println(
                        "Length vs Weight: " + uc10Feet.equals(uc10Kg)
                );



    }
}
