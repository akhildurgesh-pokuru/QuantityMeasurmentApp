
import java.math.MathContext;
import java.util.Objects;
import java.math.BigDecimal;
import java.math.RoundingMode;


public final class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Measurement must be a finite number"
            );
        }

        this.value = value;
        this.unit = Objects.requireNonNull(
                unit, "Unit cannot be null"
        );
    }

    private BigDecimal toInches() {

        return BigDecimal.valueOf(value)
                .multiply(unit.getConversionFactor())
                .setScale(6, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        QuantityLength other = (QuantityLength) obj;

        return this.toInches()
                .compareTo(other.toInches()) == 0;
    }


    public static double convert(
            double value,
            LengthUnit sourceUnit,
            LengthUnit targetUnit) {

        // Validate numerical input
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Value must be a finite number");
        }

        // Validate units
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException(
                    "Source and target units cannot be null");
        }

        // Same-unit conversion
        if (sourceUnit == targetUnit) {
            return value;
        }

        // Convert source measurement into inches
        BigDecimal baseValue = BigDecimal.valueOf(value)
                .multiply(sourceUnit.getConversionFactor());

        // Convert inches into the requested target unit
        BigDecimal convertedValue = baseValue.divide(
                targetUnit.getConversionFactor(),
                MathContext.DECIMAL64
        );

        double result = convertedValue.doubleValue();

        // Reject results outside the double range
        if (!Double.isFinite(result)) {
            throw new ArithmeticException(
                    "Converted value is outside the double range");
        }

        return result;
    }


    public QuantityLength convertTo(LengthUnit targetUnit) {

        double convertedValue = convert(
                this.value,
                this.unit,
                targetUnit
        );

        return new QuantityLength(
                convertedValue,
                targetUnit
        );
    }


}
