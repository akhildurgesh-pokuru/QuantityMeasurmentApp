
// UC15: Internal generic model.
//
// U can represent LengthUnit, WeightUnit,
// VolumeUnit or TemperatureUnit.
public final class QuantityModel<U extends IMeasurable> {

    private final double value;
    private final U unit;

    // Create an internal measurement model.
    public QuantityModel(double value, U unit) {

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "Measurement must be finite");
        }

        if (unit == null) {
            throw new IllegalArgumentException(
                    "Unit cannot be null");
        }

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    // Convert the model into the existing
    // generic Quantity object.
    //
    // This lets UC15 reuse UC10-UC14 logic.
    public Quantity<U> toQuantity() {
        return new Quantity<>(value, unit);
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}
