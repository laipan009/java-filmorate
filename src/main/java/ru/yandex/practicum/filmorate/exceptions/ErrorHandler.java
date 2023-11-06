package ru.yandex.practicum.filmorate.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValid(final MethodArgumentNotValidException e) {
        log.error("Invoke exception "+ e.getMessage());
        return Map.of("Error: Not valid parameters", "try again");
    }

    @ExceptionHandler(NotExistObjectException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotExistObject(final NotExistObjectException e) {
        log.error("Invoke exception "+ e.getMessage());
        return Map.of("Error: Not Exist Object", e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNotExistObject(final RuntimeException e) {
        log.error("Invoke exception "+ e.getMessage());
        return Map.of("Error: ", e.getMessage());
    }
}
