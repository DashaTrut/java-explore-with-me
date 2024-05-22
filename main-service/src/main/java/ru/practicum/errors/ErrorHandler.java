package ru.practicum.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.errors.exception.BadException;
import ru.practicum.errors.exception.EntityNotFoundException;
import ru.practicum.errors.exception.ValidationException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ApiError handlerEntityNotFoundException(final EntityNotFoundException exception) {
        log.info("Данные не найдены {}", exception.getMessage());
        List<String> errors = Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
        return new ApiError(errors, exception.getMessage(), "The required object was not found.",
                Response.NOT_FOUND, LocalDateTime.now());
    }

    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlerHttpMediaTypeNotAcceptableException(final HttpMediaTypeNotAcceptableException exception) {
        log.info("Данные не найдены {}", exception.getMessage());
        List<String> errors = Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
        return new ApiError(errors, exception.getMessage(), "For the requested operation the conditions are not met.",
                Response.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT) //409
    public ApiError handlerValidationException(final ValidationException exception) {
        log.info("Ошибка валидации {}", exception.getMessage());
        List<String> errors = Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
        return new ApiError(errors, exception.getMessage(), "For the requested operation the conditions are not met.",
                Response.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.warn("{}:", Response.BAD_REQUEST, exception);
        List<String> errors = Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
        return new ApiError(errors, exception.getMessage(), "Integrity constraint has been violated.",
                Response.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityException(final DataIntegrityViolationException exception) {
        log.warn("{}:", Response.CONFLICT, exception);
        List<String> errors = Arrays.stream(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
        return new ApiError(errors, exception.getMessage(), "Data integrity was violated", Response.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(final Exception e) {
        log.error("500 {}", e.getMessage(), e);
        List<String> errors = Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
        return new ApiError(errors, e.getMessage(), "Incorrectly made request.",
                Response.BAD_REQUEST, LocalDateTime.now());
    }

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


    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}


