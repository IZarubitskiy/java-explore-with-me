package ru.practicum.ewm.exeption.exemptions;

public class AlreadyConfirmedException extends RuntimeException {
    public AlreadyConfirmedException(String message) {
        super(message);
    }
}