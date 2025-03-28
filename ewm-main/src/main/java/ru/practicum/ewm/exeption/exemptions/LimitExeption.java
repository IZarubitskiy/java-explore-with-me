package ru.practicum.ewm.exeption.exemptions;

public class LimitExeption extends RuntimeException {
    public LimitExeption(String message) {
        super(message);
    }
}
