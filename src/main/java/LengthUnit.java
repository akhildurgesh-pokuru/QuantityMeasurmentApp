
import java.math.BigDecimal;

public enum LengthUnit {

    FEET("12"),
    INCHES("1"),
    YARDS("36"),
    CENTIMETERS("0.39370078740157480315");

    private final BigDecimal conversionFactor;

    LengthUnit(String factor) {
        this.conversionFactor = new BigDecimal(factor);
    }

    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }
}
