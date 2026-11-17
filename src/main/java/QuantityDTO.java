
import java.io.Serializable;

// UC15: DTO for transferring measurement data.
//
// Example:
// value = 10.0
// measurementType = "LENGTH"
// unit = "FEET"
public final class QuantityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Numerical measurement.
    private final double value;

    // Category: LENGTH, WEIGHT, VOLUME, TEMPERATURE.
    private final String measurementType;

    // Unit name: FEET, GRAM, LITRE, CELSIUS, etc.
    private final String unit;

    // Constructor initializes all DTO fields.
    public QuantityDTO(
            double value,
            String measurementType,
            String unit) {

        // Reject invalid numbers.
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Measurement must be finite");
        }

        // Validate category and unit.
        if (measurementType == null
                || measurementType.isBlank()
                || unit == null
                || unit.isBlank()) {

            throw new IllegalArgumentException(
                    "Measurement type and unit are required");
        }

        this.value = value;

        // Store category and unit in uppercase.
        this.measurementType =
                measurementType.trim().toUpperCase();

        this.unit = unit.trim().toUpperCase();
    }

    // Return numerical value.
    public double getValue() {
        return value;
    }

    // Return measurement category.
    public String getMeasurementType() {
        return measurementType;
    }

    // Return unit name.
    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}
