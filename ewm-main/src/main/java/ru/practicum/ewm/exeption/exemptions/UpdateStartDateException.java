package ru.practicum.ewm.exeption.exemptions;

public class UpdateStartDateException extends RuntimeException {
    public UpdateStartDateException(String message) {
        super(message);
    }
}