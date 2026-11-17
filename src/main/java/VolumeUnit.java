
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

// UC11: VolumeUnit defines all supported volume units.
//
// implements IMeasurable means this enum promises
// to provide every method declared in the interface.
public enum VolumeUnit implements IMeasurable {

    // Every conversion factor is relative to LITRE.
    // 1 LITRE = 1 LITRE
    // 1 MILLILITRE = 0.001 LITRE
    // 1 US GALLON ≈ 3.78541 LITRES
    LITRE("1"),
    MILLILITRE("0.001"),
    GALLON("3.78541");

    // Store the conversion factor for each enum constant.
    // final means the factor cannot be reassigned.
    private final BigDecimal conversionFactor;

    // Enum constructor.
    // Java calls it while creating each constant above.
    VolumeUnit(String factor) {

        // Convert the provided text into BigDecimal.
        this.conversionFactor = new BigDecimal(factor);
    }

    // Required by the IMeasurable interface.
    // Return the factor relative to litres.
    @Override
    public BigDecimal getConversionFactor() {

        return conversionFactor;
    }

    // Convert a measurement from its current unit
    // into the common base unit: LITRE.
    @Override
    public double convertToBaseUnit(double value) {

        // Reject NaN and positive/negative infinity.
        validate(value);

        // Formula:
        // value in litres = value * conversionFactor
        //
        // Example:
        // 1000 MILLILITRE * 0.001 = 1 LITRE
        BigDecimal result = BigDecimal.valueOf(value)
                .multiply(conversionFactor);

        // Convert BigDecimal into double.
        double converted = result.doubleValue();

        // Ensure the resulting double is valid.
        if (!Double.isFinite(converted)) {
            throw new ArithmeticException(
                    "Volume conversion overflow");
        }

        return converted;
    }

    // Convert a measurement from litres
    // into the current enum unit.
    @Override
    public double convertFromBaseUnit(double baseValue) {

        // Validate the input measurement.
        validate(baseValue);

        // Formula:
        // target value = litres / conversionFactor
        //
        // Example:
        // 1 LITRE / 0.001 = 1000 MILLILITRES
        BigDecimal result = BigDecimal.valueOf(baseValue)
                .divide(
                        conversionFactor,
                        MathContext.DECIMAL64
                );

        double converted = result.doubleValue();

        // Reject results outside the double range.
        if (!Double.isFinite(converted)) {
            throw new ArithmeticException(
                    "Volume conversion overflow");
        }

        return converted;
    }

    // Return a readable unit name.
    //
    // Example:
    // VolumeUnit.LITRE.getUnitName() returns "LITRE".
    @Override
    public String getUnitName() {

        return name();
    }

    // Required by our existing UC10 IMeasurable interface.
    //
    // Quantity.equals() uses this method to compare
    // measurements after converting them to a common unit.
    @Override
    public BigDecimal normalizedBaseValue(double value) {

        validate(value);

        // Normalize the volume into litres.
        //
        // Scale 5 means keeping five decimal places.
        // It allows the assignment's rounded gallon examples
        // to compare equal at the chosen precision.
        return BigDecimal.valueOf(value)
                .multiply(conversionFactor)
                .setScale(5, RoundingMode.HALF_UP);
    }

    // Reusable numerical input validation.
    private static void validate(double value) {

        // isFinite returns false for NaN and infinity.
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Volume must be a finite number");
        }
    }
}
