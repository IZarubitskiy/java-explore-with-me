package ru.practicum.ewm.exeption.exemptions;

public class DuplicationException extends RuntimeException {
    public DuplicationException(String message) {
        super(message);
    }
}