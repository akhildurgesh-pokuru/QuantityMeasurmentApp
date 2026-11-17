
# Quantity Measurement Application

## Project Overview

The **Quantity Measurement Application** is a Java-based project designed to perform measurement comparisons, unit conversions, and arithmetic operations across different measurement categories.

The project follows an incremental development approach, progressing from basic object-oriented programming concepts to a layered architecture with database integration and RESTful web services.

It demonstrates Java fundamentals, object-oriented design principles, generics, interfaces, design patterns, JDBC, Spring Boot, Spring Data JPA, and REST API development.

The application supports **Length, Weight, Volume, and Temperature** measurements while maintaining type safety, reusable business logic, and appropriate validation.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Core application development |
| Maven | Build and dependency management |
| JUnit 5 | Unit testing |
| Spring Boot | REST application framework |
| Spring Web MVC | RESTful API development |
| Spring Data JPA | Database persistence |
| Hibernate | Object-relational mapping |
| H2 Database | Relational database |
| JDBC | Database connectivity |
| HikariCP | Database connection pooling |
| Swagger / OpenAPI | API documentation |
| Spring Boot Actuator | Application monitoring |
| IntelliJ IDEA | Development environment |
| Git & GitHub | Version control |

## Key Features

### 1. Length Measurement

Supports the following units:

- Feet
- Inches
- Yards
- Centimeters

Features include equality comparison, unit conversion, addition, subtraction, and division.

**Example:**

```java
Quantity<LengthUnit> feet =
        new Quantity<>(1.0, LengthUnit.FEET);

Quantity<LengthUnit> inches =
        new Quantity<>(12.0, LengthUnit.INCHES);

System.out.println(feet.equals(inches)); // true
```

### 2. Weight Measurement

Supports:

- Kilograms
- Grams
- Pounds

Allows comparison, conversion, addition, subtraction, and division between compatible weight measurements.

**Example:**

```java
Quantity<WeightUnit> kg =
        new Quantity<>(1.0, WeightUnit.KILOGRAM);

Quantity<WeightUnit> grams =
        new Quantity<>(1000.0, WeightUnit.GRAM);

System.out.println(kg.equals(grams)); // true
```

### 3. Volume Measurement

Supports:

- Litres
- Millilitres
- Gallons

Provides equality comparison, unit conversion, and arithmetic operations.

**Example:**

```java
Quantity<VolumeUnit> litres =
        new Quantity<>(1.0, VolumeUnit.LITRE);

Quantity<VolumeUnit> millilitres =
        new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

System.out.println(litres.equals(millilitres)); // true
```

### 4. Temperature Measurement

Supports:

- Celsius
- Fahrenheit
- Kelvin

Temperature measurements support equality comparison and conversion between units.

Arithmetic operations on absolute temperatures are intentionally restricted.

**Example:**

```java
Quantity<TemperatureUnit> celsius =
        new Quantity<>(100.0, TemperatureUnit.CELSIUS);

System.out.println(
        celsius.convertTo(TemperatureUnit.FAHRENHEIT)
);

// Output: 212.0 FAHRENHEIT
```

### 5. Generic Measurement Framework

The application uses Java generics and the `IMeasurable` interface to provide reusable functionality across measurement categories.

```java
public final class Quantity<U extends IMeasurable>
```

This reduces duplicate logic and improves extensibility.

### 6. Arithmetic Operations

Supported operations for Length, Weight, and Volume include:

- Addition
- Subtraction
- Division

Arithmetic calculations are centralized to follow the **DRY (Don't Repeat Yourself)** principle.

Division returns a dimensionless numerical ratio.

### 7. Validation and Exception Handling

The application validates measurements and handles errors such as:

- Invalid measurement values
- Null measurement units
- Incompatible measurement categories
- Division by zero
- Unsupported temperature arithmetic
- Invalid unit names in API requests

Custom exceptions and centralized REST exception handling provide consistent error responses.

## Use Case Development

The project was developed through the following use cases:

| Use Case | Description |
|---|---|
| UC1 | Feet Measurement Equality |
| UC2 | Inches Measurement Equality |
| UC3 | Cross-Unit Length Equality |
| UC4 | Extended Length Units |
| UC5 | Unit Conversion |
| UC6 | Addition of Length Measurements |
| UC7 | Addition with Target Unit |
| UC8 | Length Unit Enum Refactoring |
| UC9 | Weight Measurement |
| UC10 | Generic Quantity Using `IMeasurable` |
| UC11 | Volume Measurement |
| UC12 | Subtraction and Division |
| UC13 | Centralized Arithmetic Logic using DRY |
| UC14 | Temperature Measurement with Selective Arithmetic Support |
| UC15 | N-Tier Architecture Refactoring |
| UC16 | JDBC Database Integration |
| UC17 | Spring Boot REST API and Spring Data JPA Integration |

## Application Architecture

The project follows a layered architecture to separate responsibilities and improve maintainability.

```text
                 Client / Postman / Swagger
                           |
                           v
                  REST Controller Layer
                           |
                           v
                      Service Layer
                           |
                           v
              Generic Quantity Business Logic
                           |
                           v
                    Repository Layer
                           |
                           v
                       H2 Database
```

### Controller Layer

Handles HTTP requests, maps API endpoints, validates incoming data, and returns responses.

### Service Layer

Contains business operations, coordinates measurement processing, and communicates with repositories.

### Model / DTO Layer

Represents measurement inputs, results, and database entities.

### Repository Layer

Provides database operations using Spring Data JPA. Earlier development stages also include cache-based and JDBC repositories.

### Core Measurement Layer

Contains the generic `Quantity<U>` implementation, measurement unit enums, conversion rules, and arithmetic validation.

## REST API Endpoints

The Spring Boot integration defines the following API endpoints:

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/quantities/compare` | Compare quantities |
| POST | `/api/v1/quantities/convert` | Convert units |
| POST | `/api/v1/quantities/add` | Add quantities |
| POST | `/api/v1/quantities/subtract` | Subtract quantities |
| POST | `/api/v1/quantities/divide` | Divide quantities |
| GET | `/api/v1/quantities/history/operation/{operation}` | Retrieve history by operation |
| GET | `/api/v1/quantities/history/type/{measurementType}` | Retrieve history by measurement category |
| GET | `/api/v1/quantities/count/{operation}` | Count successful operations |
| GET | `/api/v1/quantities/history/errored` | Retrieve failed operations |

### Example API Request

**POST** `/api/v1/quantities/add`

```json
{
  "thisQuantityDTO": {
    "value": 1.0,
    "unit": "FEET",
    "measurementType": "LengthUnit"
  },
  "thatQuantityDTO": {
    "value": 12.0,
    "unit": "INCHES",
    "measurementType": "LengthUnit"
  },
  "targetUnit": "FEET"
}
```

**Expected result:**

```json
{
  "resultQuantityDTO": {
    "value": 2.0,
    "unit": "FEET",
    "measurementType": "LengthUnit"
  },
  "error": false
}
```

The actual response also includes additional operation metadata.

## Installation and Setup

### Prerequisites

Ensure the following software is installed:

- Java Development Kit (JDK) 17
- Apache Maven
- IntelliJ IDEA or another Java IDE
- Git

### Clone the Repository

```bash
git clone <your-repository-url>
cd QuantityMeasurment
```

Replace `<your-repository-url>` with the actual GitHub repository URL.

### Build the Project

```bash
mvn clean compile
```

### Run Unit Tests

```bash
mvn clean test
```

### Start the Spring Boot Application

```bash
mvn spring-boot:run
```

The Spring Boot application is configured to run locally on port `8080`.

## API Documentation

Once the Spring Boot application is running, open:

**Swagger UI**

http://localhost:8080/swagger-ui.html

Swagger UI allows interactive exploration and testing of the REST API endpoints.

## Database Configuration

The project uses H2 as its development database.

The Spring Boot application can be configured in `src/main/resources/application.properties`:

```properties
spring.application.name=quantity-measurement-app

spring.datasource.url=jdbc:h2:mem:quantitymeasurementdb;DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**H2 Console:**

http://localhost:8080/h2-console

The in-memory database is intended for development and testing. Its data is not retained across application restarts.

## Testing

Testing is implemented using JUnit 5 and Spring Boot Test.

Test coverage includes:

- Measurement equality
- Unit conversion
- Arithmetic operations
- Null and invalid input validation
- Cross-category restrictions
- Temperature arithmetic restrictions
- Database repository operations
- REST API request and response handling

The UC1–UC16 implementation previously completed **277 JUnit tests successfully**, with no failures or errors.

UC17 introduces additional Spring Boot and REST integration tests, which require a successful Spring application setup and execution.

## Object-Oriented Principles and Design Patterns

The project demonstrates several important software engineering concepts:

- **Encapsulation:** Measurement values and units are stored in immutable objects.
- **Abstraction:** `IMeasurable` defines common measurement behavior.
- **Polymorphism:** Different unit enums provide category-specific conversion logic.
- **Generics:** `Quantity<U>` supports multiple measurement categories.
- **DRY Principle:** Common arithmetic logic is centralized.
- **Single Responsibility Principle:** Controllers, services, and repositories have separate responsibilities.
- **Dependency Injection:** Spring manages dependencies between application components.
- **Repository Pattern:** Data storage logic is separated from business logic.
- **Singleton Pattern:** Used in the earlier cache repository implementation.
- **N-Tier Architecture:** Application responsibilities are organized into distinct layers.

## Future Enhancements

Potential improvements include:

- Additional measurement categories and units
- Authentication and authorization using Spring Security
- PostgreSQL or MySQL database support
- Persistent database migrations
- Containerization using Docker
- CI/CD pipelines using GitHub Actions
- API deployment to a cloud platform
- Improved logging and monitoring
- More advanced arithmetic and temperature-difference operations

## Conclusion

The Quantity Measurement Application demonstrates the evolution of a Java project from fundamental object-oriented programming to a modern, layered REST API architecture.

Through UC1–UC17, the project explores measurement conversion, equality, arithmetic, Java generics, SOLID principles, design patterns, database integration, and Spring Boot development.

It serves as a practical learning project for understanding Java software engineering, maintainable application architecture, automated testing, and RESTful service development.
