package com.app.quantitymeasurement.controller;


import com.app.quantitymeasurement.model.QuantityInputDTO;import com.app.quantitymeasurement.model.QuantityMeasurementDTO;import com.app.quantitymeasurement.service.QuantityMeasurementSpringService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

// UC17: REST endpoints for quantity measurement.
@RestController

// Every endpoint begins with /api/v1/quantities.
@RequestMapping("/api/v1/quantities")

// Group the endpoints in Swagger documentation.
@Tag(
        name = "Quantity Measurements",
        description = "REST API for measurement operations"
)
public class QuantityMeasurementRestController {

    private final QuantityMeasurementSpringService service;

    // Spring constructor injection.
    public QuantityMeasurementRestController(
            QuantityMeasurementSpringService service) {

        this.service = service;
    }


    // ==========================================
    // POST /compare
    // ==========================================

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities")
    public QuantityMeasurementDTO compare(
            @Valid @RequestBody QuantityInputDTO input) {

        return service.compare(
                input.thisQuantityDTO(),
                input.thatQuantityDTO()
        );
    }


    // ==========================================
    // POST /convert
    // ==========================================

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity")
    public QuantityMeasurementDTO convert(
            @Valid @RequestBody QuantityInputDTO input) {

        // Accept either the explicit targetUnit field
        // or the second quantity's unit from UC17 examples.
        String target = input.targetUnit();

        if (target == null
                && input.thatQuantityDTO() != null) {

            target = input.thatQuantityDTO().unit();
        }

        if (target == null || target.isBlank()) {
            throw new IllegalArgumentException(
                    "Target unit is required");
        }

        return service.convert(
                input.thisQuantityDTO(),
                target
        );
    }


    // ==========================================
    // POST /add
    // ==========================================

    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public QuantityMeasurementDTO add(
            @Valid @RequestBody QuantityInputDTO input) {

        return service.add(
                input.thisQuantityDTO(),
                input.thatQuantityDTO(),
                input.targetUnit()
        );
    }


    // ==========================================
    // POST /subtract
    // ==========================================

    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities")
    public QuantityMeasurementDTO subtract(
            @Valid @RequestBody QuantityInputDTO input) {

        return service.subtract(
                input.thisQuantityDTO(),
                input.thatQuantityDTO(),
                input.targetUnit()
        );
    }


    // ==========================================
    // POST /divide
    // ==========================================

    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities")
    public QuantityMeasurementDTO divide(
            @Valid @RequestBody QuantityInputDTO input) {

        return service.divide(
                input.thisQuantityDTO(),
                input.thatQuantityDTO()
        );
    }


    // ==========================================
    // GET /history/operation/{operation}
    // ==========================================

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get history by operation")
    public List<QuantityMeasurementDTO> getHistory(
            @PathVariable String operation) {

        return service.getHistory(operation);
    }


    // ==========================================
    // GET /history/type/{measurementType}
    // ==========================================

    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get history by category")
    public List<QuantityMeasurementDTO> getByType(
            @PathVariable String measurementType) {

        return service.getByType(measurementType);
    }


    // ==========================================
    // GET /count/{operation}
    // ==========================================

    @GetMapping("/count/{operation}")
    @Operation(summary = "Count successful operations")
    public long count(
            @PathVariable String operation) {

        return service.countSuccessful(operation);
    }


    // ==========================================
    // GET /history/errored
    // ==========================================

    @GetMapping("/history/errored")
    @Operation(summary = "Get failed operations")
    public List<QuantityMeasurementDTO> getErrors() {

        return service.getErrors();
    }
}

