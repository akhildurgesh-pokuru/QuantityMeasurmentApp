
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// UC16: Centralized application configuration.
public final class ApplicationConfig {

    // Configuration values loaded from the resources folder.
    private final Properties properties = new Properties();

    public ApplicationConfig() {

        // Search for application.properties on the classpath.
        try (InputStream input =
                     ApplicationConfig.class
                             .getClassLoader()
                             .getResourceAsStream(
                                     "application.properties")) {

            // The configuration file must exist.
            if (input == null) {
                throw new IllegalStateException(
                        "application.properties not found");
            }

            // Read all key/value pairs.
            properties.load(input);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to load application configuration", e);
        }
    }

    // Allow JVM system properties to override file settings.
    //
    // Example:
    // -Drepository.type=cache
    public String get(String key, String defaultValue) {

        return System.getProperty(
                key,
                properties.getProperty(key, defaultValue)
        );
    }

    public int getInt(String key, int defaultValue) {

        // Convert a configuration string into an integer.
        return Integer.parseInt(
                get(key, Integer.toString(defaultValue))
        );
    }

    public String getRepositoryType() {
        return get("repository.type", "database");
    }

    public String getDatabaseUrl() {
        return get(
                "db.url",
                "jdbc:h2:file:./data/quantity_measurement"
        );
    }

    public String getDatabaseUsername() {
        return get("db.username", "sa");
    }

    public String getDatabasePassword() {
        return get("db.password", "");
    }
}
