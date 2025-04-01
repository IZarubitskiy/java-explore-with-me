package ru.practicum.ewm.exeption.exemptions;

public class InitiatorRequestException extends RuntimeException {
    public InitiatorRequestException(String message) {
        super(message);
    }
}