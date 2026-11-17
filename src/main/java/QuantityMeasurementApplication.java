

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// UC17: Spring Boot entry point.
//
// This annotation enables:
// 1. Automatic configuration
// 2. Component scanning
// 3. Spring Boot application setup
@SpringBootApplication
public class QuantityMeasurementApplication {

    public static void main(String[] args) {

        // Start the Spring application.
        //
        // Spring automatically creates:
        // - REST controllers
        // - Services
        // - JPA repositories
        // - Database connections
        // - Embedded Tomcat server
        SpringApplication.run(
                QuantityMeasurementApplication.class,
                args
        );
    }
}
