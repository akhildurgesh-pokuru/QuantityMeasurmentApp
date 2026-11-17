
import java.util.List;

// UC15: Repository contract.
//
// The service depends on this interface,
// not on a particular storage implementation.
public interface IQuantityMeasurementRepository {

    // Save an operation result or error.
    void save(QuantityMeasurementEntity entity);

    // Retrieve all stored operation records.
    List<QuantityMeasurementEntity> getAllMeasurements();
}
