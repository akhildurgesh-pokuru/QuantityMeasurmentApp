
import java.util.List;

// UC15: N-Tier application entry point.
//
// This class only initializes the dependencies,
// creates requests, and invokes the controller.
public class QuantityMeasurementUC15App {

    public static void main(String[] args) {

        System.out.println(
                "\n===== UC15: N-TIER ARCHITECTURE =====");


        // ==========================================
        // Step 1: Initialize Repository
        // ==========================================

        // Obtain the singleton cache repository.
        IQuantityMeasurementRepository repository =
                QuantityMeasurementCacheRepository.getInstance();


        // ==========================================
        // Step 2: Initialize Service
        // ==========================================

        // Inject the repository into the service.
        IQuantityMeasurementService service =
                new QuantityMeasurementServiceImpl(repository);


        // ==========================================
        // Step 3: Initialize Controller
        // ==========================================

        // Inject the service into the controller.
        QuantityMeasurementController controller =
                new QuantityMeasurementController(service);


        // ==========================================
        // Step 4: Length Equality
        // ==========================================

        System.out.println("\n--- Length Equality ---");

        // Create DTOs representing 1 foot and 12 inches.
        QuantityDTO feet =
                new QuantityDTO(1.0, "LENGTH", "FEET");

        QuantityDTO inches =
                new QuantityDTO(12.0, "LENGTH", "INCHES");

        // Controller -> Service -> Result.
        controller.displayResult(
                controller.performComparison(feet, inches)
        );


        // ==========================================
        // Step 5: Length Conversion
        // ==========================================

        System.out.println("\n--- Length Conversion ---");

        // Convert 1 foot to inches.
        controller.displayResult(
                controller.performConversion(feet, "INCHES")
        );


        // ==========================================
        // Step 6: Length Addition
        // ==========================================

        System.out.println("\n--- Length Addition ---");

        // 1 foot + 12 inches = 2 feet.
        controller.displayResult(
                controller.performAddition(
                        feet, inches, "FEET"
                )
        );


        // ==========================================
        // Step 7: Weight Subtraction
        // ==========================================

        System.out.println("\n--- Weight Subtraction ---");

        QuantityDTO kg =
                new QuantityDTO(
                        10.0, "WEIGHT", "KILOGRAM");

        QuantityDTO grams =
                new QuantityDTO(
                        5000.0, "WEIGHT", "GRAM");

        // 10 kg - 5000 grams = 5 kg.
        controller.displayResult(
                controller.performSubtraction(
                        kg, grams, "KILOGRAM"
                )
        );


        // ==========================================
        // Step 8: Volume Division
        // ==========================================

        System.out.println("\n--- Volume Division ---");

        QuantityDTO litres =
                new QuantityDTO(
                        5.0, "VOLUME", "LITRE");

        QuantityDTO millilitres =
                new QuantityDTO(
                        500.0, "VOLUME", "MILLILITRE");

        // 5 litres / 500 millilitres = 10.
        controller.displayResult(
                controller.performDivision(
                        litres, millilitres
                )
        );


        // ==========================================
        // Step 9: Temperature Conversion
        // ==========================================

        System.out.println("\n--- Temperature Conversion ---");

        QuantityDTO celsius =
                new QuantityDTO(
                        100.0, "TEMPERATURE", "CELSIUS");

        // 100 Celsius = 212 Fahrenheit.
        controller.displayResult(
                controller.performConversion(
                        celsius, "FAHRENHEIT"
                )
        );


        // ==========================================
        // Step 10: Unsupported Temperature Addition
        // ==========================================

        System.out.println(
                "\n--- Unsupported Temperature Addition ---");

        QuantityDTO fiftyCelsius =
                new QuantityDTO(
                        50.0, "TEMPERATURE", "CELSIUS");

        // Temperature arithmetic is disabled in UC14.
        // Service catches the exception and returns
        // a standardized error entity.
        controller.displayResult(
                controller.performAddition(
                        celsius, fiftyCelsius, "CELSIUS"
                )
        );


        // ==========================================
        // Step 11: Cross-Category Prevention
        // ==========================================

        System.out.println("\n--- Category Validation ---");

        // A length cannot be added to a weight.
        controller.displayResult(
                controller.performAddition(
                        feet, kg, "FEET"
                )
        );


        // ==========================================
        // Step 12: Display Repository History
        // ==========================================

        System.out.println("\n--- Saved Operation History ---");

        List<QuantityMeasurementEntity> history =
                repository.getAllMeasurements();

        // Records are retained between executions
        // through the serialized repository file.
        for (QuantityMeasurementEntity record : history) {
            System.out.println(
                    record.getTimestamp() + " | " + record
            );
        }

        System.out.println(
                "\n===== UC15 COMPLETED =====");
    }
}
