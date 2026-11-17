package com.app.quantitymeasurement.repository;


import com.app.quantitymeasurement.model.QuantityMeasurementEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// UC17: JPA repository.
//
// Spring automatically implements database methods.
// No manual JDBC connection is required.
@Repository
public interface QuantityMeasurementRepository
        extends JpaRepository<QuantityMeasurementEntity, Long> {

    // SELECT records matching an operation.
    List<QuantityMeasurementEntity> findByOperation(
            String operation);

    // SELECT records matching a measurement category.
    List<QuantityMeasurementEntity> findByFirstType(
            String firstType);

    // Find all unsuccessful operations.
    List<QuantityMeasurementEntity> findByErrorTrue();

    // Count operation records that succeeded.
    long countByOperationAndErrorFalse(String operation);
}
