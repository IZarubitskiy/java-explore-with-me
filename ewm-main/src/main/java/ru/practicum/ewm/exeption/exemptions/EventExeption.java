package ru.practicum.ewm.exeption.exemptions;

public class EventExeption extends RuntimeException {
    public EventExeption(String message) {
        super(message);
    }
}