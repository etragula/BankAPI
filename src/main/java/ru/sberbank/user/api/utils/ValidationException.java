package ru.sberbank.user.api.utils;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
