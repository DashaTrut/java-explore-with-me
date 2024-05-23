package ru.practicum.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.errors.exception.BadException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(final BadException e) {
        log.error("500 {}", e.getMessage(), e);
        List<String> errors = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
        return new ApiError(errors, e.getMessage(), "Incorrectly made request.",
                Response.BAD_REQUEST, LocalDateTime.now());
    }
}
