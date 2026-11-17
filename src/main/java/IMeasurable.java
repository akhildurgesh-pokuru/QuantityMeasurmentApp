
import java.math.BigDecimal;

public interface IMeasurable {

    // Return the unit conversion factor
    BigDecimal getConversionFactor();

    // Convert a measurement to its category's base unit
    double convertToBaseUnit(double value);

    // Convert the base unit into a selected unit
    double convertFromBaseUnit(double baseValue);

    // Return the readable unit name
    String getUnitName();

    // Normalize values for equality and hashing
    BigDecimal normalizedBaseValue(double value);
}
