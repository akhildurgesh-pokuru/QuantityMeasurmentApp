package com.app.quantitymeasurement.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

// UC17: Input containing one or two quantities.
//
// Conversion uses first + targetUnit.
// Arithmetic uses first + second.
// Addition/subtraction may use targetUnit.
public record QuantityInputDTO(

        @Valid
        @NotNull(message = "First quantity is required")
        com.app.quantitymeasurement.model.QuantityDTO thisQuantityDTO,

        @Valid
        com.app.quantitymeasurement.model.QuantityDTO thatQuantityDTO,

        String targetUnit
) {
}

