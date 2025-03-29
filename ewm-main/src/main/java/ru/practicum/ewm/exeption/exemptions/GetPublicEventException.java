package ru.practicum.ewm.exeption.exemptions;

public class GetPublicEventException extends RuntimeException {
    public GetPublicEventException(String message) {
        super(message);
    }
}