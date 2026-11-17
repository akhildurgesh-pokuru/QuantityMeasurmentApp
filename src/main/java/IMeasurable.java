
import java.math.BigDecimal;

public interface IMeasurable {

    // Conversion factor already used by our existing enums
    BigDecimal getConversionFactor();

    // Convert this unit into its category's base unit
    double convertToBaseUnit(double value);

    // Convert from the category's base unit to this unit
    double convertFromBaseUnit(double baseValue);

    // Return a readable unit name
    String getUnitName();

    // Return a normalized value for equality and hashing
    BigDecimal normalizedBaseValue(double value);
}
