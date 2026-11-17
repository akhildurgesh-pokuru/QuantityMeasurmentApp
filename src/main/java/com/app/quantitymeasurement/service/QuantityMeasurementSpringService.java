package com.app.quantitymeasurement.service;


// Import measurement classes from the core package.
import com.app.quantitymeasurement.core.*;

// Import REST exception.
import com.app.quantitymeasurement.exception.QuantityApiException;

// Import DTO and entity classes.
import com.app.quantitymeasurement.model.*;

// Import JPA repository.
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;

// Spring annotation.
import org.springframework.stereotype.Service;

// Java utilities.
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;


// UC17: Spring-managed business service.
//
// Reuses Quantity<U> for arithmetic and conversion.
// Uses Spring Data JPA for history.
@Service
public class QuantityMeasurementSpringService {

    private final QuantityMeasurementRepository repository;

    // Constructor injection.
    //
    // Spring automatically supplies the JPA repository.
    public QuantityMeasurementSpringService(
            QuantityMeasurementRepository repository) {

        this.repository = repository;
    }


    // ==========================================
    // Convert a DTO into an existing core unit
    // ==========================================

    private IMeasurable resolveUnit(QuantityDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Quantity cannot be null");
        }

        String category =
                dto.measurementType()
                        .trim()
                        .toUpperCase(Locale.ROOT);

        String unitName =
                dto.unit()
                        .trim()
                        .toUpperCase(Locale.ROOT);

        // Accept both the names used in the UC17
        // assignment and the earlier UC15 names.
        switch (category) {

            case "LENGTH":
            case "LENGTHUNIT":
                return LengthUnit.valueOf(unitName);

            case "WEIGHT":
            case "WEIGHTUNIT":
                return WeightUnit.valueOf(unitName);

            case "VOLUME":
            case "VOLUMEUNIT":
                return VolumeUnit.valueOf(unitName);

            case "TEMPERATURE":
            case "TEMPERATUREUNIT":
                return TemperatureUnit.valueOf(unitName);

            default:
                throw new IllegalArgumentException(
                        "Invalid measurement type: "
                                + dto.measurementType());
        }
    }


    // ==========================================
    // DTO -> Generic Quantity
    // ==========================================

    private Quantity<IMeasurable> toQuantity(
            QuantityDTO dto) {

        IMeasurable unit = resolveUnit(dto);

        return new Quantity<>(
                dto.value(),
                unit
        );
    }


    // ==========================================
    // Generic Quantity -> DTO
    // ==========================================

    private QuantityDTO toDTO(
            Quantity<IMeasurable> quantity) {

        IMeasurable unit = quantity.getUnit();

        String measurementType;

        if (unit instanceof LengthUnit) {
            measurementType = "LengthUnit";

        } else if (unit instanceof WeightUnit) {
            measurementType = "WeightUnit";

        } else if (unit instanceof VolumeUnit) {
            measurementType = "VolumeUnit";

        } else if (unit instanceof TemperatureUnit) {
            measurementType = "TemperatureUnit";

        } else {
            throw new IllegalArgumentException(
                    "Unknown unit category");
        }

        return new QuantityDTO(
                quantity.getValue(),
                unit.getUnitName(),
                measurementType
        );
    }


    // ==========================================
    // Check measurement-category compatibility
    // ==========================================

    private void validateSameCategory(
            Quantity<IMeasurable> first,
            Quantity<IMeasurable> second) {

        if (first.getUnit().getClass()
                != second.getUnit().getClass()) {

            throw new IllegalArgumentException(
                    "Cannot perform operation between "
                            + "different measurement categories");
        }
    }


    // ==========================================
    // Common operation execution
    // ==========================================

    private QuantityMeasurementDTO execute(
            String operation,
            QuantityDTO first,
            QuantityDTO second,
            Consumer<QuantityMeasurementEntity> calculation) {

        // Entity records the request before execution.
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity(
                        operation,
                        first,
                        second
                );

        try {

            // Perform the calculation.
            calculation.accept(entity);

        } catch (RuntimeException exception) {

            // Save the failure before returning HTTP 400.
            //
            // This fulfils UC17's error-history requirement.
            entity.setErrorMessage(exception.getMessage());

            repository.save(entity);

            throw new QuantityApiException(
                    operation + " Error: "
                            + exception.getMessage(),
                    exception
            );
        }

        // Save successful operation.
        QuantityMeasurementEntity saved =
                repository.save(entity);

        // Return the API response DTO.
        return QuantityMeasurementDTO.fromEntity(saved);
    }


    // ==========================================
    // UC17: Compare Measurements
    // ==========================================

    public QuantityMeasurementDTO compare(
            QuantityDTO first,
            QuantityDTO second) {

        return execute("COMPARE", first, second, entity -> {

            Quantity<IMeasurable> a = toQuantity(first);
            Quantity<IMeasurable> b = toQuantity(second);

            // The REST service rejects incompatible categories.
            validateSameCategory(a, b);

            // Existing UC10 equality logic.
            entity.setComparisonResult(a.equals(b));
        });
    }


    // ==========================================
    // UC17: Convert Measurements
    // ==========================================

    public QuantityMeasurementDTO convert(
            QuantityDTO first,
            String targetUnit) {

        return execute("CONVERT", first, null, entity -> {

            Quantity<IMeasurable> original =
                    toQuantity(first);

            // Resolve target within the same category.
            QuantityDTO targetDescription =
                    new QuantityDTO(
                            0.0,
                            targetUnit,
                            first.measurementType()
                    );

            IMeasurable target =
                    resolveUnit(targetDescription);

            // Existing UC10 conversion logic.
            Quantity<IMeasurable> result =
                    original.convertTo(target);

            entity.setQuantityResult(toDTO(result));
        });
    }


    // ==========================================
    // UC17: Add Measurements
    // ==========================================

    public QuantityMeasurementDTO add(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return execute("ADD", first, second, entity -> {

            Quantity<IMeasurable> a = toQuantity(first);
            Quantity<IMeasurable> b = toQuantity(second);

            validateSameCategory(a, b);

            // When no target is supplied, use
            // the first measurement's unit.
            IMeasurable target = targetUnit == null
                    ? a.getUnit()
                    : resolveUnit(
                    new QuantityDTO(
                            0.0,
                            targetUnit,
                            first.measurementType()
                    )
            );

            // The core class checks whether arithmetic
            // is allowed for this measurement category.
            Quantity<IMeasurable> result =
                    a.add(b, target);

            entity.setQuantityResult(toDTO(result));
        });
    }


    // ==========================================
    // UC17: Subtract Measurements
    // ==========================================

    public QuantityMeasurementDTO subtract(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit) {

        return execute("SUBTRACT", first, second, entity -> {

            Quantity<IMeasurable> a = toQuantity(first);
            Quantity<IMeasurable> b = toQuantity(second);

            validateSameCategory(a, b);

            IMeasurable target = targetUnit == null
                    ? a.getUnit()
                    : resolveUnit(
                    new QuantityDTO(
                            0.0,
                            targetUnit,
                            first.measurementType()
                    )
            );

            Quantity<IMeasurable> result =
                    a.subtract(b, target);

            entity.setQuantityResult(toDTO(result));
        });
    }


    // ==========================================
    // UC17: Divide Measurements
    // ==========================================

    public QuantityMeasurementDTO divide(
            QuantityDTO first,
            QuantityDTO second) {

        return execute("DIVIDE", first, second, entity -> {

            Quantity<IMeasurable> a = toQuantity(first);
            Quantity<IMeasurable> b = toQuantity(second);

            validateSameCategory(a, b);

            // Division returns a dimensionless double.
            double ratio = a.divide(b);

            entity.setScalarResult(ratio);
        });
    }


    // ==========================================
    // UC17: Retrieve operation history
    // ==========================================

    public List<QuantityMeasurementDTO> getHistory(
            String operation) {

        // Map database entities into response DTOs.
        return repository.findByOperation(
                        operation.toUpperCase(Locale.ROOT))
                .stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .toList();
    }


    // ==========================================
    // UC17: Retrieve history by category
    // ==========================================

    public List<QuantityMeasurementDTO> getByType(
            String measurementType) {

        return repository.findAll()
                .stream()
                .filter(entity ->
                        entity.getFirstType() != null
                                && normalizeType(
                                entity.getFirstType())
                                .equals(normalizeType(measurementType)))
                .map(QuantityMeasurementDTO::fromEntity)
                .toList();
    }

    // Convert LENGTH and LengthUnit into one
    // comparison-friendly representation.
    private String normalizeType(String type) {

        String normalized =
                type.trim().toUpperCase(Locale.ROOT);

        return normalized.endsWith("UNIT")
                ? normalized.substring(
                0, normalized.length() - 4)
                : normalized;
    }


    // ==========================================
    // UC17: Count successful operations
    // ==========================================

    public long countSuccessful(String operation) {

        return repository.countByOperationAndErrorFalse(
                operation.toUpperCase(Locale.ROOT)
        );
    }


    // ==========================================
    // UC17: Error history
    // ==========================================

    public List<QuantityMeasurementDTO> getErrors() {

        return repository.findByErrorTrue()
                .stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .toList();
    }
}

