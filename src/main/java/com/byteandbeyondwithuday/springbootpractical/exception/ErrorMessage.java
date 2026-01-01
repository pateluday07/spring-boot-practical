package com.byteandbeyondwithuday.springbootpractical.exception;

public enum ErrorMessage {

    EMPLOYEE_NOT_FOUND("Employee not found with the ID: %d"),
    REMOVE_EMPLOYEE_ID_FROM_REQUEST("Do not include Employee ID on create"),
    EMPLOYEE_ID_MUST_BE_PROVIDED("Employee ID must be provided for update"),
    EMPLOYEE_EMAIL_ALREADY_EXISTS("Employee with email %s already exists");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String formatMessage(Object... args) {
        return String.format(this.message, args);
    }
}
