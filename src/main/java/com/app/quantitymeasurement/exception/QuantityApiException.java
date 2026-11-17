package com.app.quantitymeasurement.exception;


// UC17: Business-level REST exception.
public class QuantityApiException extends RuntimeException {

    public QuantityApiException(String message) {
        super(message);
    }

    public QuantityApiException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}

