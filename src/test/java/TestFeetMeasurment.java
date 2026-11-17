
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFeetMeasurment {

    @Test
    public void testEqualFeet() {
        FeetMeasurment.Feet feet1 = new FeetMeasurment.Feet(1.0);
        FeetMeasurment.Feet feet2 = new FeetMeasurment.Feet(1.0);
        FeetMeasurment.Feet feet3 = new FeetMeasurment.Feet(2.0);

        assertEquals(true,feet1.equals(feet2));
        assertEquals(false, feet1.equals(feet3));
    }
}
