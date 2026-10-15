package com.smartcampus.erp.exception;

// Rubric: Exception Handling (Custom Exception)
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String field, String reason) {
        super(String.format("Invalid data for field '%s': %s", field, reason));
    }
}
