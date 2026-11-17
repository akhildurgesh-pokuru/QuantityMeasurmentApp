
import java.util.List;
import java.util.logging.Logger;

// UC16: Application entry point with JDBC persistence.
public class QuantityMeasurementUC16App {

    // Java's built-in logger.
    private static final Logger LOGGER =
            Logger.getLogger(
                    QuantityMeasurementUC16App.class.getName());

    public static void main(String[] args) {

        // Load repository and database configuration.
        ApplicationConfig config = new ApplicationConfig();

        IQuantityMeasurementRepository repository;

        // Decide whether this run uses H2 or the old cache.
        if ("database".equalsIgnoreCase(
                config.getRepositoryType())) {

            // Initialize pooled connections.
            ConnectionPool pool = new ConnectionPool(config);

            // Create JDBC repository.
            // Its constructor also initializes the schema.
            repository =
                    new QuantityMeasurementDatabaseRepository(pool);

            LOGGER.info("Using H2 JDBC database repository");

        } else if ("cache".equalsIgnoreCase(
                config.getRepositoryType())) {

            // Continue supporting the UC15 cache.
            repository =
                    QuantityMeasurementCacheRepository.getInstance();

            LOGGER.info("Using UC15 cache repository");

        } else {
            throw new IllegalArgumentException(
                    "Unknown repository.type: "
                            + config.getRepositoryType());
        }

        try {

            // Inject the repository into the service.
            IQuantityMeasurementService service =
                    new QuantityMeasurementServiceImpl(repository);

            // Inject the service into the controller.
            QuantityMeasurementController controller =
                    new QuantityMeasurementController(service);


            // ======================================
            // Length Comparison
            // ======================================

            QuantityDTO oneFoot =
                    new QuantityDTO(
                            1.0, "LENGTH", "FEET");

            QuantityDTO twelveInches =
                    new QuantityDTO(
                            12.0, "LENGTH", "INCHES");

            // Equality: 1 foot = 12 inches.
            controller.displayResult(
                    controller.performComparison(
                            oneFoot,
                            twelveInches
                    )
            );


            // ======================================
            // Length Addition
            // ======================================

            // 1 foot + 12 inches = 2 feet.
            controller.displayResult(
                    controller.performAddition(
                            oneFoot,
                            twelveInches,
                            "FEET"
                    )
            );


            // ======================================
            // Weight Subtraction
            // ======================================

            QuantityDTO kg =
                    new QuantityDTO(
                            10.0, "WEIGHT", "KILOGRAM");

            QuantityDTO grams =
                    new QuantityDTO(
                            5000.0, "WEIGHT", "GRAM");

            // 10 kilograms - 5000 grams = 5 kilograms.
            controller.displayResult(
                    controller.performSubtraction(
                            kg,
                            grams,
                            "KILOGRAM"
                    )
            );


            // ======================================
            // Temperature Conversion
            // ======================================

            QuantityDTO celsius =
                    new QuantityDTO(
                            100.0, "TEMPERATURE", "CELSIUS");

            // 100 Celsius = 212 Fahrenheit.
            controller.displayResult(
                    controller.performConversion(
                            celsius,
                            "FAHRENHEIT"
                    )
            );


            // ======================================
            // Unsupported Temperature Addition
            // ======================================

            QuantityDTO fiftyCelsius =
                    new QuantityDTO(
                            50.0, "TEMPERATURE", "CELSIUS");

            // UC14 rejects absolute temperature addition.
            controller.displayResult(
                    controller.performAddition(
                            celsius,
                            fiftyCelsius,
                            "CELSIUS"
                    )
            );


            // ======================================
            // Retrieve Stored History
            // ======================================

            LOGGER.info("Total measurements: "
                    + repository.getTotalCount());

            List<QuantityMeasurementEntity> history =
                    repository.getAllMeasurements();

            // Print records loaded from the repository.
            for (QuantityMeasurementEntity entity : history) {

                LOGGER.info(entity.toString());
            }

            // Show pooling statistics when using JDBC.
            LOGGER.info("Repository statistics: "
                    + repository.getPoolStatistics());

        } finally {

            // Always close repository-managed resources.
            repository.releaseResources();
        }
    }
}
