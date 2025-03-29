package ru.practicum.ewm.exeption.exemptions;

public class EventRequestExeption extends RuntimeException {
    public EventRequestExeption(String message) {
        super(message);
    }
}
