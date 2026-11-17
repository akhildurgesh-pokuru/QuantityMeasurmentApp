
import java.util.function.Supplier;

// UC15: Service implementation.
//
// Responsibilities:
// 1. Accept DTO input
// 2. Convert DTO to internal model
// 3. Validate measurement categories
// 4. Perform the operation
// 5. Save success or error records
// 6. Return a standardized entity
public class QuantityMeasurementServiceImpl
        implements IQuantityMeasurementService {

    // Depend on the repository interface.
    private final IQuantityMeasurementRepository repository;

    // Constructor injection.
    public QuantityMeasurementServiceImpl(
            IQuantityMeasurementRepository repository) {

        if (repository == null) {
            throw new IllegalArgumentException(
                    "Repository cannot be null");
        }

        this.repository = repository;
    }


    // ==========================================
    // Convert a unit name into an IMeasurable
    // ==========================================

    private IMeasurable resolveUnit(
            String category,
            String unitName) {

        if (category == null || unitName == null) {
            throw new IllegalArgumentException(
                    "Category and unit cannot be null");
        }

        // Convert input into uppercase.
        String type = category.trim().toUpperCase();
        String name = unitName.trim().toUpperCase();

        // Choose the correct existing enum.
        try {
            switch (type) {

                case "LENGTH":
                    return LengthUnit.valueOf(name);

                case "WEIGHT":
                    return WeightUnit.valueOf(name);

                case "VOLUME":
                    return VolumeUnit.valueOf(name);

                case "TEMPERATURE":
                    return TemperatureUnit.valueOf(name);

                default:
                    throw new IllegalArgumentException(
                            "Unknown measurement category: " + type);
            }

        } catch (IllegalArgumentException e) {

            throw new QuantityMeasurementException(
                    "Invalid measurement type or unit: "
                            + type + "/" + name, e);
        }
    }


    // ==========================================
    // DTO -> Internal QuantityModel
    // ==========================================

    private QuantityModel<IMeasurable> toModel(
            QuantityDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Quantity DTO cannot be null");
        }

        // Resolve the requested unit.
        IMeasurable unit = resolveUnit(
                dto.getMeasurementType(),
                dto.getUnit()
        );

        return new QuantityModel<>(
                dto.getValue(),
                unit
        );
    }


    // ==========================================
    // Internal Quantity -> DTO
    // ==========================================

    private QuantityDTO toDTO(
            Quantity<IMeasurable> quantity) {

        IMeasurable unit = quantity.getUnit();

        // Determine the category from the unit enum.
        String category;

        if (unit instanceof LengthUnit) {
            category = "LENGTH";

        } else if (unit instanceof WeightUnit) {
            category = "WEIGHT";

        } else if (unit instanceof VolumeUnit) {
            category = "VOLUME";

        } else if (unit instanceof TemperatureUnit) {
            category = "TEMPERATURE";

        } else {
            throw new IllegalArgumentException(
                    "Unsupported measurement category");
        }

        return new QuantityDTO(
                quantity.getValue(),
                category,
                unit.getUnitName()
        );
    }


    // ==========================================
    // Validate compatible measurement categories
    // ==========================================

    private void validateSameCategory(
            Quantity<IMeasurable> first,
            Quantity<IMeasurable> second) {

        // All enum constants of a category share a class.
        if (first.getUnit().getClass()
                != second.getUnit().getClass()) {

            throw new IllegalArgumentException(
                    "Cannot mix different measurement categories");
        }
    }


    // ==========================================
    // Common Operation Execution
    // ==========================================

    private QuantityMeasurementEntity execute(
            String operation,
            QuantityDTO first,
            QuantityDTO second,
            Supplier<QuantityMeasurementEntity> action) {

        QuantityMeasurementEntity entity;

        try {

            // Perform the requested business operation.
            entity = action.get();

        } catch (RuntimeException e) {

            // Wrap the original problem in our
            // application-specific exception type.
            QuantityMeasurementException error =
                    new QuantityMeasurementException(
                            e.getMessage(), e);

            // Represent the error as data.
            entity = QuantityMeasurementEntity.error(
                    operation,
                    first,
                    second,
                    error.getMessage()
            );
        }

        // Store both successes and operation errors.
        repository.save(entity);

        return entity;
    }


    // ==========================================
    // Equality Comparison
    // ==========================================

    @Override
    public QuantityMeasurementEntity compare(
            QuantityDTO first,
            QuantityDTO second) {

        return execute(
                "COMPARE", first, second, () -> {

                    // Convert input DTOs into typed models.
                    QuantityModel<IMeasurable> firstModel =
                            toModel(first);

                    QuantityModel<IMeasurable> secondModel =
                            toModel(second);

                    // Obtain reusable Quantity objects.
                    Quantity<IMeasurable> q1 =
                            firstModel.toQuantity();

                    Quantity<IMeasurable> q2 =
                            secondModel.toQuantity();

                    // UC15 explicitly rejects category mixing.
                    validateSameCategory(q1, q2);

                    // Reuse existing equality logic.
                    boolean equal = q1.equals(q2);

                    return QuantityMeasurementEntity.comparison(
                            first, second, equal);
                }
        );
    }


    // ==========================================
    // Unit Conversion
    // ==========================================

    @Override
    public QuantityMeasurementEntity convert(
            QuantityDTO quantity,
            String targetUnit) {

        return execute(
                "CONVERT", quantity, null, () -> {

                    Quantity<IMeasurable> original =
                            toModel(quantity).toQuantity();

                    // Resolve target within the input category.
                    IMeasurable target = resolveUnit(
                            quantity.getMeasurementType(),
                            targetUnit
                    );

                    // Existing Quantity.convertTo()
                    // handles conversion through the base unit.
                    Quantity<IMeasurable> converted =
                            original.convertTo(target);

                    return QuantityMeasurementEntity.quantityResult(
                            "CONVERT",
                            quantity,
                            null,
                            toDTO(converted)
                    );
                }
        );
    }


    // ==========================================
    // Addition
    // ==========================================

    @Override
    public QuantityMeasurementEntity add(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return execute(
                "ADD", first, second, () -> {

                    Quantity<IMeasurable> q1 =
                            toModel(first).toQuantity();

                    Quantity<IMeasurable> q2 =
                            toModel(second).toQuantity();

                    validateSameCategory(q1, q2);

                    // Use first quantity's unit when
                    // no explicit target is supplied.
                    IMeasurable target = targetUnit == null
                            ? q1.getUnit()
                            : resolveUnit(
                            first.getMeasurementType(),
                            targetUnit);

                    // Reuse UC13 centralized addition.
                    Quantity<IMeasurable> result =
                            q1.add(q2, target);

                    return QuantityMeasurementEntity.quantityResult(
                            "ADD", first, second, toDTO(result)
                    );
                }
        );
    }


    // ==========================================
    // Subtraction
    // ==========================================

    @Override
    public QuantityMeasurementEntity subtract(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return execute(
                "SUBTRACT", first, second, () -> {

                    Quantity<IMeasurable> q1 =
                            toModel(first).toQuantity();

                    Quantity<IMeasurable> q2 =
                            toModel(second).toQuantity();

                    validateSameCategory(q1, q2);

                    // Default: result uses first operand's unit.
                    IMeasurable target = targetUnit == null
                            ? q1.getUnit()
                            : resolveUnit(
                            first.getMeasurementType(),
                            targetUnit);

                    // Reuse existing subtraction logic.
                    Quantity<IMeasurable> result =
                            q1.subtract(q2, target);

                    return QuantityMeasurementEntity.quantityResult(
                            "SUBTRACT",
                            first,
                            second,
                            toDTO(result)
                    );
                }
        );
    }


    // ==========================================
    // Division
    // ==========================================

    @Override
    public QuantityMeasurementEntity divide(
            QuantityDTO first,
            QuantityDTO second) {

        return execute(
                "DIVIDE", first, second, () -> {

                    Quantity<IMeasurable> q1 =
                            toModel(first).toQuantity();

                    Quantity<IMeasurable> q2 =
                            toModel(second).toQuantity();

                    validateSameCategory(q1, q2);

                    // Division returns a dimensionless double.
                    double ratio = q1.divide(q2);

                    return QuantityMeasurementEntity.division(
                            first, second, ratio);
                }
        );
    }
}
