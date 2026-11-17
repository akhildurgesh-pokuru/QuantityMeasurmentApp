
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuantityUC15Test {

    private IQuantityMeasurementRepository repository;
    private IQuantityMeasurementService service;
    private QuantityMeasurementController controller;

    // Test repository that stores data only in memory.
    private static class TestRepository
            implements IQuantityMeasurementRepository {

        private final List<QuantityMeasurementEntity> records =
                new ArrayList<>();

        @Override
        public void save(QuantityMeasurementEntity entity) {
            records.add(entity);
        }

        @Override
        public List<QuantityMeasurementEntity> getAllMeasurements() {
            return new ArrayList<>(records);
        }
    }

    // Create fresh dependencies before each test.
    @BeforeEach
    void setUp() {

        repository = new TestRepository();

        service = new QuantityMeasurementServiceImpl(repository);

        controller = new QuantityMeasurementController(service);
    }


    @Test
    void testDTO_StoresMeasurement() {

        QuantityDTO dto =
                new QuantityDTO(1.0, "LENGTH", "FEET");

        assertEquals(1.0, dto.getValue());
        assertEquals("LENGTH", dto.getMeasurementType());
        assertEquals("FEET", dto.getUnit());
    }


    @Test
    void testModel_ConvertsToQuantity() {

        QuantityModel<LengthUnit> model =
                new QuantityModel<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> quantity = model.toQuantity();

        assertEquals(1.0, quantity.getValue());
        assertEquals(LengthUnit.FEET, quantity.getUnit());
    }


    @Test
    void testService_CompareEquivalentLengths() {

        QuantityDTO feet =
                new QuantityDTO(1.0, "LENGTH", "FEET");

        QuantityDTO inches =
                new QuantityDTO(12.0, "LENGTH", "INCHES");

        QuantityMeasurementEntity result =
                service.compare(feet, inches);

        assertFalse(result.hasError());
        assertEquals(Boolean.TRUE, result.getComparisonResult());
    }


    @Test
    void testService_ConvertFeetToInches() {

        QuantityDTO feet =
                new QuantityDTO(1.0, "LENGTH", "FEET");

        QuantityMeasurementEntity result =
                service.convert(feet, "INCHES");

        assertFalse(result.hasError());
        assertEquals(12.0, result.getResult().getValue(), 1e-6);
        assertEquals("INCHES", result.getResult().getUnit());
    }


    @Test
    void testService_AddDifferentUnits() {

        QuantityDTO feet =
                new QuantityDTO(1.0, "LENGTH", "FEET");

        QuantityDTO inches =
                new QuantityDTO(12.0, "LENGTH", "INCHES");

        QuantityMeasurementEntity result =
                service.add(feet, inches, "FEET");

        assertFalse(result.hasError());
        assertEquals(2.0, result.getResult().getValue(), 1e-6);
    }


    @Test
    void testService_SubtractWeight() {

        QuantityDTO kg =
                new QuantityDTO(10.0, "WEIGHT", "KILOGRAM");

        QuantityDTO grams =
                new QuantityDTO(5000.0, "WEIGHT", "GRAM");

        QuantityMeasurementEntity result =
                service.subtract(kg, grams, "KILOGRAM");

        assertFalse(result.hasError());
        assertEquals(5.0, result.getResult().getValue(), 1e-6);
    }


    @Test
    void testService_DivideVolume() {

        QuantityDTO litres =
                new QuantityDTO(5.0, "VOLUME", "LITRE");

        QuantityDTO ml =
                new QuantityDTO(500.0, "VOLUME", "MILLILITRE");

        QuantityMeasurementEntity result =
                service.divide(litres, ml);

        assertFalse(result.hasError());
        assertEquals(10.0, result.getScalarResult(), 1e-6);
    }


    @Test
    void testService_TemperatureConversion() {

        QuantityDTO celsius =
                new QuantityDTO(100.0, "TEMPERATURE", "CELSIUS");

        QuantityMeasurementEntity result =
                service.convert(celsius, "FAHRENHEIT");

        assertFalse(result.hasError());
        assertEquals(212.0, result.getResult().getValue(), 1e-6);
    }


    @Test
    void testService_UnsupportedTemperatureAddition() {

        QuantityDTO first =
                new QuantityDTO(100.0, "TEMPERATURE", "CELSIUS");

        QuantityDTO second =
                new QuantityDTO(50.0, "TEMPERATURE", "CELSIUS");

        QuantityMeasurementEntity result =
                service.add(first, second, "CELSIUS");

        // An invalid operation becomes an error entity.
        assertTrue(result.hasError());
        assertNotNull(result.getErrorMessage());
    }


    @Test
    void testService_CrossCategoryRejected() {

        QuantityDTO feet =
                new QuantityDTO(1.0, "LENGTH", "FEET");

        QuantityDTO kg =
                new QuantityDTO(1.0, "WEIGHT", "KILOGRAM");

        QuantityMeasurementEntity result =
                service.add(feet, kg, "FEET");

        assertTrue(result.hasError());
    }


    @Test
    void testService_DivisionByZero() {

        QuantityDTO first =
                new QuantityDTO(10.0, "LENGTH", "FEET");

        QuantityDTO zero =
                new QuantityDTO(0.0, "LENGTH", "INCHES");

        QuantityMeasurementEntity result =
                service.divide(first, zero);

        assertTrue(result.hasError());
    }


    @Test
    void testRepository_SavesSuccessAndError() {

        QuantityDTO feet =
                new QuantityDTO(1.0, "LENGTH", "FEET");

        QuantityDTO inches =
                new QuantityDTO(12.0, "LENGTH", "INCHES");

        QuantityDTO kg =
                new QuantityDTO(1.0, "WEIGHT", "KILOGRAM");

        // One successful operation.
        service.compare(feet, inches);

        // One incompatible operation.
        service.add(feet, kg, "FEET");

        // Both should be recorded.
        assertEquals(
                2,
                repository.getAllMeasurements().size()
        );
    }


    @Test
    void testController_DelegatesToService() {

        QuantityDTO feet =
                new QuantityDTO(1.0, "LENGTH", "FEET");

        QuantityDTO inches =
                new QuantityDTO(12.0, "LENGTH", "INCHES");

        QuantityMeasurementEntity result =
                controller.performAddition(
                        feet, inches, "FEET");

        assertFalse(result.hasError());
        assertEquals(2.0, result.getResult().getValue(), 1e-6);
    }


    @Test
    void testController_NullServiceRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new QuantityMeasurementController(null)
        );
    }


    @Test
    void testService_NullRepositoryRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new QuantityMeasurementServiceImpl(null)
        );
    }


    @Test
    void testEntity_ErrorRepresentation() {

        QuantityMeasurementEntity error =
                QuantityMeasurementEntity.error(
                        "ADD", null, null,
                        "Invalid measurement");

        assertTrue(error.hasError());
        assertTrue(error.toString().contains("Invalid measurement"));
    }
}
