
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// UC16: JDBC database repository.
//
// Uses:
// - PreparedStatement for SQL parameters
// - JDBC transactions for consistent writes
// - HikariCP for reusable database connections
// - The same repository interface from UC15
public final class QuantityMeasurementDatabaseRepository
        implements IQuantityMeasurementRepository {

    private final ConnectionPool pool;


    // ==========================================
    // Constructor
    // ==========================================

    public QuantityMeasurementDatabaseRepository(
            ConnectionPool pool) {

        if (pool == null) {
            throw new IllegalArgumentException(
                    "Connection pool cannot be null");
        }

        this.pool = pool;

        // Initialize tables when the repository starts.
        initializeSchema();
    }


    // ==========================================
    // Create Database Tables
    // ==========================================

    private void initializeSchema() {

        // Load the SQL file from resources.
        try (InputStream input =
                     getClass().getClassLoader()
                             .getResourceAsStream("db/schema.sql")) {

            if (input == null) {
                throw new DatabaseException(
                        "db/schema.sql not found");
            }

            // Read the complete SQL script.
            String sql = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            // The supplied schema consists of simple
            // CREATE TABLE and CREATE INDEX statements.
            // Remove line comments before splitting.
            StringBuilder cleaned = new StringBuilder();

            for (String line : sql.split("\\R")) {

                int commentStart = line.indexOf("--");

                if (commentStart >= 0) {
                    line = line.substring(0, commentStart);
                }

                cleaned.append(line).append('\n');
            }

            try (Connection connection = pool.getConnection();
                 Statement statement = connection.createStatement()) {

                // Execute each SQL statement separately.
                for (String command :
                        cleaned.toString().split(";")) {

                    String query = command.trim();

                    if (!query.isEmpty()) {
                        statement.execute(query);
                    }
                }
            }

        } catch (IOException | SQLException e) {
            throw new DatabaseException(
                    "Failed to initialize database schema", e);
        }
    }


    // ==========================================
    // Save a Measurement
    // ==========================================

    @Override
    public void save(QuantityMeasurementEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "Entity cannot be null");
        }

        String insertSql = """
                INSERT INTO quantity_measurement_entity (
                    operation,
                    first_value, first_type, first_unit,
                    second_value, second_type, second_unit,
                    result_value, result_type, result_unit,
                    comparison_result, scalar_result,
                    error_message, created_at
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String historySql = """
                INSERT INTO quantity_measurement_history
                    (measurement_id, operation)
                VALUES (?, ?)
                """;

        try (Connection connection = pool.getConnection()) {

            // Disable auto-commit so both inserts
            // become part of one transaction.
            connection.setAutoCommit(false);

            try {
                long measurementId;

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     insertSql,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    // Store operation name.
                    statement.setString(
                            1, entity.getOperation());

                    // Store first input measurement.
                    setQuantity(statement, 2, entity.getFirst());

                    // Store optional second measurement.
                    setQuantity(statement, 5, entity.getSecond());

                    // Store optional result measurement.
                    setQuantity(statement, 8, entity.getResult());

                    // Store comparison result.
                    setNullableBoolean(
                            statement, 11,
                            entity.getComparisonResult());

                    // Store division ratio.
                    setNullableDouble(
                            statement, 12,
                            entity.getScalarResult());

                    // Store error text.
                    statement.setString(
                            13, entity.getErrorMessage());

                    // Preserve original operation timestamp.
                    statement.setString(
                            14, entity.getTimestamp());

                    // Insert the main record.
                    statement.executeUpdate();

                    // Retrieve the generated primary key.
                    try (ResultSet keys =
                                 statement.getGeneratedKeys()) {

                        if (!keys.next()) {
                            throw new SQLException(
                                    "No generated measurement ID");
                        }

                        measurementId = keys.getLong(1);
                    }
                }

                // Insert the matching audit record.
                try (PreparedStatement history =
                             connection.prepareStatement(historySql)) {

                    history.setLong(1, measurementId);
                    history.setString(
                            2, entity.getOperation());

                    history.executeUpdate();
                }

                // Save both records together.
                connection.commit();

            } catch (SQLException | RuntimeException e) {

                // Undo the transaction on failure.
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;

            } finally {

                // Restore connection state before returning
                // it to the pool.
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Unable to save measurement", e);
        }
    }


    // ==========================================
    // Shared PreparedStatement Helpers
    // ==========================================

    private void setQuantity(
            PreparedStatement statement,
            int startIndex,
            QuantityDTO quantity) throws SQLException {

        if (quantity == null) {

            statement.setNull(
                    startIndex, Types.DOUBLE);

            statement.setNull(
                    startIndex + 1, Types.VARCHAR);

            statement.setNull(
                    startIndex + 2, Types.VARCHAR);

            return;
        }

        statement.setDouble(
                startIndex,
                quantity.getValue());

        statement.setString(
                startIndex + 1,
                quantity.getMeasurementType());

        statement.setString(
                startIndex + 2,
                quantity.getUnit());
    }

    private void setNullableBoolean(
            PreparedStatement statement,
            int index,
            Boolean value) throws SQLException {

        if (value == null) {
            statement.setNull(index, Types.BOOLEAN);
        } else {
            statement.setBoolean(index, value);
        }
    }

    private void setNullableDouble(
            PreparedStatement statement,
            int index,
            Double value) throws SQLException {

        if (value == null) {
            statement.setNull(index, Types.DOUBLE);
        } else {
            statement.setDouble(index, value);
        }
    }


    // ==========================================
    // Read QuantityDTO From ResultSet
    // ==========================================

    private QuantityDTO readQuantity(
            ResultSet result,
            String prefix) throws SQLException {

        // The type is null when the quantity
        // was absent from the saved operation.
        String type = result.getString(
                prefix + "_type");

        if (type == null) {
            return null;
        }

        return new QuantityDTO(
                result.getDouble(prefix + "_value"),
                type,
                result.getString(prefix + "_unit")
        );
    }


    // ==========================================
    // Convert SQL Row Back Into Entity
    // ==========================================

    private QuantityMeasurementEntity mapRow(
            ResultSet result) throws SQLException {

        QuantityDTO first =
                readQuantity(result, "first");

        QuantityDTO second =
                readQuantity(result, "second");

        QuantityDTO quantityResult =
                readQuantity(result, "result");

        // Read nullable comparison result.
        Boolean comparison = (Boolean) result.getObject(
                "comparison_result");

        // Read nullable division ratio.
        Double ratio = (Double) result.getObject(
                "scalar_result");

        // Rebuild the operation entity with its
        // original timestamp.
        return QuantityMeasurementEntity.fromDatabase(
                result.getString("operation"),
                first,
                second,
                quantityResult,
                comparison,
                ratio,
                result.getString("error_message"),
                result.getString("created_at")
        );
    }


    // ==========================================
    // Common Query Helper
    // ==========================================

    private List<QuantityMeasurementEntity> find(
            String sql,
            String parameter) {

        List<QuantityMeasurementEntity> measurements =
                new ArrayList<>();

        try (Connection connection = pool.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Bind the optional query parameter.
            //
            // This prevents SQL injection because
            // input is treated as data, not SQL code.
            if (parameter != null) {
                statement.setString(1, parameter);
            }

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    // Convert every returned SQL row
                    // into an operation entity.
                    measurements.add(mapRow(result));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Unable to retrieve measurements", e);
        }

        return measurements;
    }


    // ==========================================
    // Retrieve All Measurements
    // ==========================================

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {

        return find(
                """
                SELECT * FROM quantity_measurement_entity
                ORDER BY id
                """,
                null
        );
    }


    // ==========================================
    // Find Records by Operation
    // ==========================================

    @Override
    public List<QuantityMeasurementEntity>
    getMeasurementsByOperation(String operation) {

        if (operation == null) {
            throw new IllegalArgumentException(
                    "Operation cannot be null");
        }

        return find(
                """
                SELECT * FROM quantity_measurement_entity
                WHERE operation = ?
                ORDER BY id
                """,
                operation.trim().toUpperCase(Locale.ROOT)
        );
    }


    // ==========================================
    // Find Records by Measurement Type
    // ==========================================

    @Override
    public List<QuantityMeasurementEntity>
    getMeasurementsByType(String measurementType) {

        if (measurementType == null) {
            throw new IllegalArgumentException(
                    "Measurement type cannot be null");
        }

        return find(
                """
                SELECT * FROM quantity_measurement_entity
                WHERE first_type = ?
                ORDER BY id
                """,
                measurementType.trim().toUpperCase(Locale.ROOT)
        );
    }


    // ==========================================
    // Count Stored Measurements
    // ==========================================

    @Override
    public long getTotalCount() {

        String sql = """
                SELECT COUNT(*)
                FROM quantity_measurement_entity
                """;

        try (Connection connection = pool.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            result.next();

            return result.getLong(1);

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Unable to count measurements", e);
        }
    }


    // ==========================================
    // Delete All Measurements
    // ==========================================

    @Override
    public void deleteAll() {

        try (Connection connection = pool.getConnection()) {

            connection.setAutoCommit(false);

            try (Statement statement =
                         connection.createStatement()) {

                // Delete audit records first because
                // they reference the main table.
                statement.executeUpdate(
                        "DELETE FROM quantity_measurement_history");

                statement.executeUpdate(
                        "DELETE FROM quantity_measurement_entity");

                connection.commit();

            } catch (SQLException e) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Unable to delete measurement history", e);
        }
    }


    // ==========================================
    // Connection Pool Statistics
    // ==========================================

    @Override
    public String getPoolStatistics() {

        return pool.getPoolStatistics();
    }


    // ==========================================
    // Release Resources
    // ==========================================

    @Override
    public void releaseResources() {

        pool.close();
    }
}
