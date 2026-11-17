
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// UC15: Singleton repository.
//
// Stores operation history in memory and on disk.
public final class QuantityMeasurementCacheRepository
        implements IQuantityMeasurementRepository {

    // Only one repository instance is created.
    private static final QuantityMeasurementCacheRepository
            INSTANCE = new QuantityMeasurementCacheRepository();

    // File used to preserve history after restart.
    private static final String FILE_NAME =
            "quantity-measurement-history.ser";

    // In-memory cache.
    private final List<QuantityMeasurementEntity> measurements =
            new ArrayList<>();

    // Private constructor prevents external creation.
    private QuantityMeasurementCacheRepository() {

        // Restore existing records when the app starts.
        loadFromDisk();
    }

    // Global access to the singleton.
    public static QuantityMeasurementCacheRepository getInstance() {
        return INSTANCE;
    }

    // Save a new record.
    @Override
    public synchronized void save(
            QuantityMeasurementEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "Entity cannot be null");
        }

        // Add the record to the in-memory list.
        measurements.add(entity);

        // Persist the updated history.
        saveToDisk();
    }

    // Return a defensive copy of the cache.
    @Override
    public synchronized List<QuantityMeasurementEntity>
    getAllMeasurements() {

        return new ArrayList<>(measurements);
    }

    // Serialize the entire current history.
    private void saveToDisk() {

        try (ObjectOutputStream output =
                     new ObjectOutputStream(
                             new FileOutputStream(FILE_NAME))) {

            output.writeObject(measurements);

        } catch (IOException e) {
            throw new QuantityMeasurementException(
                    "Unable to save measurement history", e);
        }
    }

    // Load previously stored history.
    @SuppressWarnings("unchecked")
    private void loadFromDisk() {

        File file = new File(FILE_NAME);

        // On first execution, the file won't exist.
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream input =
                     new ObjectInputStream(
                             new FileInputStream(file))) {

            Object stored = input.readObject();

            if (!(stored instanceof List<?>)) {
                throw new IOException(
                        "Invalid repository data");
            }

            List<?> records = (List<?>) stored;

            // Only accept operation records.
            for (Object record : records) {
                if (!(record instanceof QuantityMeasurementEntity)) {
                    throw new IOException(
                            "Invalid measurement record");
                }
            }

            // Restore the validated list.
            for (Object record : records) {
                measurements.add(
                        (QuantityMeasurementEntity) record);
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new QuantityMeasurementException(
                    "Unable to load measurement history", e);
        }
    }
}
