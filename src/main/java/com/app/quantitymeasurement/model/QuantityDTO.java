
package com.app.quantitymeasurement.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// UC17: Represents one measurement in a JSON request.
//
// Example:
// {
//   "value": 1.0,
//   "unit": "FEET",
//   "measurementType": "LengthUnit"
// }
public record QuantityDTO(

        @NotNull(message = "Value is required")
        Double value,

        @NotBlank(message = "Unit is required")
        String unit,

        @NotBlank(message = "Measurement type is required")
        String measurementType
) {

    // Reject NaN and infinity.
    @AssertTrue(message = "Value must be a finite number")
    public boolean isFiniteValue() {

        return value == null || Double.isFinite(value);
    }
}
