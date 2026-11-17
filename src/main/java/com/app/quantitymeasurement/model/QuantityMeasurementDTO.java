package com.app.quantitymeasurement.model;


import java.time.LocalDateTime;

// UC17: Standard JSON response for every operation.
public record QuantityMeasurementDTO(

        Long id,

        String operation,

        com.app.quantitymeasurement.model.QuantityDTO thisQuantityDTO,

        com.app.quantitymeasurement.model.QuantityDTO thatQuantityDTO,

        com.app.quantitymeasurement.model.QuantityDTO resultQuantityDTO,

        Boolean comparisonResult,

        Double scalarResult,

        boolean error,

        String errorMessage,

        LocalDateTime createdAt
) {

    // Convert a database entity to an API DTO.
    public static QuantityMeasurementDTO fromEntity(
            QuantityMeasurementEntity entity) {

        com.app.quantitymeasurement.model.QuantityDTO first = toQuantity(
                entity.getFirstValue(),
                entity.getFirstUnit(),
                entity.getFirstType()
        );

        com.app.quantitymeasurement.model.QuantityDTO second = toQuantity(
                entity.getSecondValue(),
                entity.getSecondUnit(),
                entity.getSecondType()
        );

        com.app.quantitymeasurement.model.QuantityDTO result = toQuantity(
                entity.getResultValue(),
                entity.getResultUnit(),
                entity.getResultType()
        );

        return new QuantityMeasurementDTO(
                entity.getId(),
                entity.getOperation(),
                first,
                second,
                result,
                entity.getComparisonResult(),
                entity.getScalarResult(),
                entity.isError(),
                entity.getErrorMessage(),
                entity.getCreatedAt()
        );
    }

    // Rebuild a measurement only when it exists.
    private static com.app.quantitymeasurement.model.QuantityDTO toQuantity(
            Double value,
            String unit,
            String type) {

        if (value == null || unit == null || type == null) {
            return null;
        }

        return new com.app.quantitymeasurement.model.QuantityDTO(value, unit, type);
    }
}
