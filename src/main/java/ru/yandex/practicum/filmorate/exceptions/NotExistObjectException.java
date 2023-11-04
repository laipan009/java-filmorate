package ru.yandex.practicum.filmorate.exceptions;

public class NotExistObjectException extends RuntimeException {
    public NotExistObjectException(String message) {
        super(message);
    }
}
