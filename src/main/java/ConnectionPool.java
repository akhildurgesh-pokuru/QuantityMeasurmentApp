
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

// UC16: JDBC connection pool.
//
// AutoCloseable allows try-with-resources.
public final class ConnectionPool implements AutoCloseable {

    // HikariCP manages the actual connections.
    private final HikariDataSource dataSource;

    public ConnectionPool(ApplicationConfig config) {

        this(
                config.getDatabaseUrl(),
                config.getDatabaseUsername(),
                config.getDatabasePassword(),
                config.getInt("db.pool.maximumSize", 5),
                config.getInt("db.pool.minimumIdle", 1)
        );
    }

    // Extra constructor makes test database setup easier.
    public ConnectionPool(
            String url,
            String username,
            String password,
            int maximumSize,
            int minimumIdle) {

        // Validate pool configuration.
        if (maximumSize < 1
                || minimumIdle < 0
                || minimumIdle > maximumSize) {

            throw new IllegalArgumentException(
                    "Invalid connection pool configuration");
        }

        // Create HikariCP configuration.
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        // Maximum number of pooled connections.
        hikariConfig.setMaximumPoolSize(maximumSize);

        // Minimum number of idle connections.
        hikariConfig.setMinimumIdle(minimumIdle);

        // Maximum time to wait for an available connection.
        hikariConfig.setConnectionTimeout(10000);

        hikariConfig.setPoolName("QuantityMeasurementPool");

        // Initialize the actual data source.
        this.dataSource = new HikariDataSource(hikariConfig);
    }

    // Borrow a connection from the pool.
    //
    // When the caller closes it, HikariCP
    // returns it to the pool for reuse.
    public Connection getConnection() {

        try {
            return dataSource.getConnection();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Unable to acquire database connection", e);
        }
    }

    // Return basic pool statistics.
    public String getPoolStatistics() {

        return "Active="
                + dataSource.getHikariPoolMXBean()
                .getActiveConnections()
                + ", Idle="
                + dataSource.getHikariPoolMXBean()
                .getIdleConnections()
                + ", Total="
                + dataSource.getHikariPoolMXBean()
                .getTotalConnections();
    }

    // Release all connections when the app exits.
    @Override
    public void close() {

        dataSource.close();
    }
}
