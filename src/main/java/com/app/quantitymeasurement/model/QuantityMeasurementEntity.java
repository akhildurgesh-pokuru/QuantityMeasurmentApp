package com.app.quantitymeasurement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// UC17: Database entity mapped through Hibernate/JPA.
@Entity
@Table(
        name = "quantity_measurements",
        indexes = {
                @Index(
                        name = "idx_operation",
                        columnList = "operation"
                ),
                @Index(
                        name = "idx_measurement_type",
                        columnList = "first_type"
                )
        }
)
public class QuantityMeasurementEntity {

    // Database-generated primary key.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String operation;

    // First input measurement.
    private Double firstValue;

    @Column(name = "first_type")
    private String firstType;

    private String firstUnit;

    // Optional second input measurement.
    private Double secondValue;
    private String secondType;
    private String secondUnit;

    // Quantity result for addition/subtraction/conversion.
    private Double resultValue;
    private String resultType;
    private String resultUnit;

    // Boolean result for comparison.
    private Boolean comparisonResult;

    // Dimensionless result for division.
    private Double scalarResult;

    // Error information.
    private boolean error;

    @Column(length = 2000)
    private String errorMessage;

    // Operation creation time.
    private LocalDateTime createdAt;

    // JPA requires a no-argument constructor.
    protected QuantityMeasurementEntity() {
    }

    // Create a record containing an operation and its inputs.
    public QuantityMeasurementEntity(
            String operation,
            com.app.quantitymeasurement.model.QuantityDTO first,
            com.app.quantitymeasurement.model.QuantityDTO second) {

        this.operation = operation;

        // Store first measurement.
        if (first != null) {
            firstValue = first.value();
            firstUnit = first.unit();
            firstType = first.measurementType();
        }

        // Store second measurement if supplied.
        if (second != null) {
            secondValue = second.value();
            secondUnit = second.unit();
            secondType = second.measurementType();
        }
    }

    // Called automatically before the first INSERT.
    @PrePersist
    public void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Store a result containing a measurement.
    public void setQuantityResult(com.app.quantitymeasurement.model.QuantityDTO result) {

        resultValue = result.value();
        resultUnit = result.unit();
        resultType = result.measurementType();
    }

    // Store a comparison result.
    public void setComparisonResult(Boolean result) {

        comparisonResult = result;
    }

    // Store a division ratio.
    public void setScalarResult(Double result) {

        scalarResult = result;
    }

    // Record an error.
    public void setErrorMessage(String message) {

        error = true;
        errorMessage = message;
    }

    // Getters are used by JPA mappings and DTO conversion.
    public Long getId() {
        return id;
    }

    public String getOperation() {
        return operation;
    }

    public Double getFirstValue() {
        return firstValue;
    }

    public String getFirstType() {
        return firstType;
    }

    public String getFirstUnit() {
        return firstUnit;
    }

    public Double getSecondValue() {
        return secondValue;
    }

    public String getSecondType() {
        return secondType;
    }

    public String getSecondUnit() {
        return secondUnit;
    }

    public Double getResultValue() {
        return resultValue;
    }

    public String getResultType() {
        return resultType;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public Boolean getComparisonResult() {
        return comparisonResult;
    }

    public Double getScalarResult() {
        return scalarResult;
    }

    public boolean isError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

