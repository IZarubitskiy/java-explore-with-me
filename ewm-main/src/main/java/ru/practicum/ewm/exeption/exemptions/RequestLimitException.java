package ru.practicum.ewm.exeption.exemptions;

public class RequestLimitException extends RuntimeException {
    public RequestLimitException(String message) {
        super(message);
    }
}