package ru.practicum.errors.exception;

public class BadException extends RuntimeException {
    public BadException() {
    }

    public BadException(String message) {
        super(message);
    }
}
