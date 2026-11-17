
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

    public static void main(String[] args) {

        // UC1: Feet equality
        System.out.println("Feet equality: "
                + compareFeet(1.0, 1.0));

        // UC2: Inches equality
        System.out.println("Inches equality: "
                + compareInches(1.0, 1.0));

        // UC3: Feet to Inches equality
        System.out.println("Feet to Inches: "
                + compareLengths(
                1.0, LengthUnit.FEET,
                12.0, LengthUnit.INCHES));

        // UC3: Different measurements
        System.out.println("Different lengths: "
                + compareLengths(
                1.0, LengthUnit.FEET,
                6.0, LengthUnit.INCHES));
    }
}
