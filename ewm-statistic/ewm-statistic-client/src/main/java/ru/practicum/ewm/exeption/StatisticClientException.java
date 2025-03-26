package ru.practicum.ewm.exeption;

public class StatisticClientException extends RuntimeException {
    public StatisticClientException(Integer code, String message) {
        super("StatusCode = " + code + "\nMessage = " + message);
    }
}
