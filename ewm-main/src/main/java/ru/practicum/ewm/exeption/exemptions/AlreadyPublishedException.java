package ru.practicum.ewm.exeption.exemptions;

public class AlreadyPublishedException extends RuntimeException {
    public AlreadyPublishedException(String message) {
        super(message);
    }
}