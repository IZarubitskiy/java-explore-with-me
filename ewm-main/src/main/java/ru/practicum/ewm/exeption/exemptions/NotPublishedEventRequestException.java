package ru.practicum.ewm.exeption.exemptions;

public class NotPublishedEventRequestException extends RuntimeException {
    public NotPublishedEventRequestException(String message) {
        super(message);
    }
}