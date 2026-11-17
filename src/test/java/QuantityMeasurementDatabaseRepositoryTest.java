
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

// UC16: Tests the JDBC repository using H2.
class QuantityMeasurementDatabaseRepositoryTest {

    private ConnectionPool pool;

    private QuantityMeasurementDatabaseRepository repository;

    private IQuantityMeasurementService service;


    // ==========================================
    // Initialize Fresh Database for Every Test
    // ==========================================

    @BeforeEach
    void setUp() {

        // Use a unique database name for each test.
        // This prevents test records from mixing.
        String databaseName =
                "quantity_test_" +
                        UUID.randomUUID().toString().replace("-", "");

        String url =
                "jdbc:h2:mem:" + databaseName +
                        ";DB_CLOSE_DELAY=-1";

        // Use a small connection pool for tests.
        pool = new ConnectionPool(
                url,
                "sa",
                "",
                3,
                1
        );

        // Constructor creates the tables automatically.
        repository =
                new QuantityMeasurementDatabaseRepository(pool);

        // Use the real service with the test repository.
        service = new QuantityMeasurementServiceImpl(repository);
    }


    // ==========================================
    // Close Pool After Every Test
    // ==========================================

    @AfterEach
    void tearDown() {

        if (pool != null) {
            pool.close();
        }
    }


    @Test
    void testDatabase_InitiallyEmpty() {

        // A new test database should have no records.
        assertEquals(0, repository.getTotalCount());
    }


    @Test
    void testDatabase_SaveComparison() {

        QuantityDTO first =
                new QuantityDTO(
                        1.0, "LENGTH", "FEET");

        QuantityDTO second =
                new QuantityDTO(
                        12.0, "LENGTH", "INCHES");

        // Service performs operation and saves the result.
        QuantityMeasurementEntity result =
                service.compare(first, second);

        assertFalse(result.hasError());

        // One record must exist in H2.
        assertEquals(1, repository.getTotalCount());

        // The original result must be stored correctly.
        QuantityMeasurementEntity saved =
                repository.getAllMeasurements().get(0);

        assertEquals("COMPARE", saved.getOperation());

        assertEquals(
                Boolean.TRUE,
                saved.getComparisonResult()
        );
    }


    @Test
    void testDatabase_SaveAddition() {

        QuantityDTO feet =
                new QuantityDTO(
                        1.0, "LENGTH", "FEET");

        QuantityDTO inches =
                new QuantityDTO(
                        12.0, "LENGTH", "INCHES");

        // 1 foot + 12 inches = 2 feet.
        service.add(feet, inches, "FEET");

        QuantityMeasurementEntity saved =
                repository.getAllMeasurements().get(0);

        assertEquals("ADD", saved.getOperation());

        assertEquals(
                2.0,
                saved.getResult().getValue(),
                1e-6
        );

        assertEquals(
                "FEET",
                saved.getResult().getUnit()
        );
    }


    @Test
    void testDatabase_QueryByOperation() {

        QuantityDTO feet =
                new QuantityDTO(
                        1.0, "LENGTH", "FEET");

        QuantityDTO inches =
                new QuantityDTO(
                        12.0, "LENGTH", "INCHES");

        // Store two different operations.
        service.compare(feet, inches);

        service.add(feet, inches, "FEET");

        List<QuantityMeasurementEntity> additions =
                repository.getMeasurementsByOperation("ADD");

        // Only addition records should be returned.
        assertEquals(1, additions.size());

        assertEquals(
                "ADD",
                additions.get(0).getOperation()
        );
    }


    @Test
    void testDatabase_QueryByMeasurementType() {

        QuantityDTO feet =
                new QuantityDTO(
                        1.0, "LENGTH", "FEET");

        QuantityDTO inches =
                new QuantityDTO(
                        12.0, "LENGTH", "INCHES");

        QuantityDTO kg =
                new QuantityDTO(
                        1.0, "WEIGHT", "KILOGRAM");

        QuantityDTO grams =
                new QuantityDTO(
                        1000.0, "WEIGHT", "GRAM");

        // Save one length operation and one weight operation.
        service.compare(feet, inches);

        service.compare(kg, grams);

        List<QuantityMeasurementEntity> lengthRecords =
                repository.getMeasurementsByType("LENGTH");

        assertEquals(1, lengthRecords.size());
    }


    @Test
    void testDatabase_DivisionResult() {

        QuantityDTO first =
                new QuantityDTO(
                        10.0, "LENGTH", "FEET");

        QuantityDTO second =
                new QuantityDTO(
                        5.0, "LENGTH", "FEET");

        service.divide(first, second);

        QuantityMeasurementEntity saved =
                repository.getAllMeasurements().get(0);

        // Division produces a dimensionless scalar.
        assertEquals(
                2.0,
                saved.getScalarResult(),
                1e-6
        );
    }


    @Test
    void testDatabase_SaveError() {

        QuantityDTO first =
                new QuantityDTO(
                        100.0, "TEMPERATURE", "CELSIUS");

        QuantityDTO second =
                new QuantityDTO(
                        50.0, "TEMPERATURE", "CELSIUS");

        // Temperature addition should fail.
        service.add(first, second, "CELSIUS");

        QuantityMeasurementEntity saved =
                repository.getAllMeasurements().get(0);

        // UC16 also persists failed operation records.
        assertTrue(saved.hasError());

        assertNotNull(saved.getErrorMessage());
    }


    @Test
    void testDatabase_DeleteAll() {

        QuantityDTO feet =
                new QuantityDTO(
                        1.0, "LENGTH", "FEET");

        QuantityDTO inches =
                new QuantityDTO(
                        12.0, "LENGTH", "INCHES");

        service.compare(feet, inches);

        assertEquals(1, repository.getTotalCount());

        // Clear both measurement and history tables.
        repository.deleteAll();

        assertEquals(0, repository.getTotalCount());
    }


    @Test
    void testDatabase_SQLInjectionPrevention() {

        QuantityDTO feet =
                new QuantityDTO(
                        1.0, "LENGTH", "FEET");

        QuantityDTO inches =
                new QuantityDTO(
                        12.0, "LENGTH", "INCHES");

        service.compare(feet, inches);

        // The input must be treated as plain text,
        // not as part of the SQL query.
        List<QuantityMeasurementEntity> results =
                repository.getMeasurementsByOperation(
                        "COMPARE' OR '1'='1"
                );

        assertTrue(results.isEmpty());

        // The original record must still exist.
        assertEquals(1, repository.getTotalCount());
    }


    @Test
    void testDatabase_ConnectionPoolStatistics() {

        String statistics =
                repository.getPoolStatistics();

        assertNotNull(statistics);

        assertTrue(statistics.contains("Active="));

        assertTrue(statistics.contains("Idle="));
    }


    @Test
    void testDatabase_OriginalTimestampPreserved() {

        QuantityDTO feet =
                new QuantityDTO(
                        1.0, "LENGTH", "FEET");

        QuantityDTO inches =
                new QuantityDTO(
                        12.0, "LENGTH", "INCHES");

        QuantityMeasurementEntity original =
                service.compare(feet, inches);

        QuantityMeasurementEntity stored =
                repository.getAllMeasurements().get(0);

        // Retrieving from the database must not
        // generate a different timestamp.
        assertEquals(
                original.getTimestamp(),
                stored.getTimestamp()
        );
    }
}
