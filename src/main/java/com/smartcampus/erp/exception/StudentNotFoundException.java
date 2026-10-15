package com.smartcampus.erp.exception;

// Rubric: Exception Handling (Custom Exception)
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(Long id) {
        super("Student with ID " + id + " was not found.");
    }
}
