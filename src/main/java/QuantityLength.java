
import java.util.Objects;

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

    private double toFeet() {
        return value * unit.getConversionFactor();
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

        return Double.compare(
                this.toFeet(),
                other.toFeet()
        ) == 0;
    }
}
