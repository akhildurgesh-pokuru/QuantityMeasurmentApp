
// UC15: Controller layer.
//
// Responsibilities:
// 1. Receive measurement requests
// 2. Call the service
// 3. Return operation results
// 4. Display results when used by the console app
public class QuantityMeasurementController {

    // Depend on the service interface,
    // not on its concrete implementation.
    private final IQuantityMeasurementService service;

    // Dependency injection through the constructor.
    public QuantityMeasurementController(
            IQuantityMeasurementService service) {

        if (service == null) {
            throw new IllegalArgumentException(
                    "Service cannot be null");
        }

        this.service = service;
    }

    // Compare two measurements.
    public QuantityMeasurementEntity performComparison(
            QuantityDTO first,
            QuantityDTO second) {

        return service.compare(first, second);
    }

    // Convert one measurement to another unit.
    public QuantityMeasurementEntity performConversion(
            QuantityDTO quantity,
            String targetUnit) {

        return service.convert(quantity, targetUnit);
    }

    // Add measurements.
    public QuantityMeasurementEntity performAddition(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return service.add(first, second, targetUnit);
    }

    // Subtract measurements.
    public QuantityMeasurementEntity performSubtraction(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return service.subtract(first, second, targetUnit);
    }

    // Divide measurements.
    public QuantityMeasurementEntity performDivision(
            QuantityDTO first,
            QuantityDTO second) {

        return service.divide(first, second);
    }

    // Present an operation result to the console.
    public void displayResult(
            QuantityMeasurementEntity entity) {

        if (entity == null) {
            System.out.println("No result available");
            return;
        }

        // Errors are formatted in the entity's toString().
        System.out.println(entity);
    }
}
