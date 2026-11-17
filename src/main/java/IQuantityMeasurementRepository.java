
import java.util.List;

// Shared contract for both cache and JDBC repositories.
public interface IQuantityMeasurementRepository {

    // Existing UC15 operations.
    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> getAllMeasurements();

    // UC16: Retrieve records for a particular operation.
    default List<QuantityMeasurementEntity>
    getMeasurementsByOperation(String operation) {

        throw new UnsupportedOperationException(
                "Operation filtering is not implemented");
    }

    // UC16: Retrieve records by measurement category.
    default List<QuantityMeasurementEntity>
    getMeasurementsByType(String measurementType) {

        throw new UnsupportedOperationException(
                "Type filtering is not implemented");
    }

    // UC16: Count stored records.
    default long getTotalCount() {
        return getAllMeasurements().size();
    }

    // UC16: Delete all records.
    default void deleteAll() {

        throw new UnsupportedOperationException(
                "Delete is not implemented");
    }

    // UC16: Optional connection-pool statistics.
    default String getPoolStatistics() {
        return "Connection pooling not used";
    }

    // UC16: Optional cleanup.
    default void releaseResources() {
        // Nothing to close by default.
    }
}
