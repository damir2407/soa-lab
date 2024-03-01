package ru.itmo.soa.soaspacemarinejava.rest;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import ru.itmo.soa.soaspacemarinejava.exception.ResourceAlreadyExistException;
import ru.itmo.soa.soaspacemarinejava.exception.ResourceNotFoundException;
import ru.itmo.soa.soaspacemarinejava.rest.data.ErrorResponse;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var fields = e.getBindingResult().getFieldErrors();
        return new ErrorResponse(
            "Неправильно введенны поля! " + fields.stream().map(FieldError::getField).reduce((s1, s2) -> s1 + ", " + s2)
                .orElse(""),
            LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        var fields = e.getConstraintViolations().stream()
            .map(it -> ((PathImpl) it.getPropertyPath()).getLeafNode().getName())
            .reduce((s1, s2) -> s1 + ", " + s2)
            .orElse("");
        return new ErrorResponse(
            "Неправильно введенны поля! " + fields,
            LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        var fields = "";
        if (e.getRootCause() instanceof MissingKotlinParameterException) {
            fields = ((MissingKotlinParameterException) e.getRootCause()).getPath().stream()
                .map(Reference::getFieldName)
                .reduce((s1, s2) -> s1 + "." + s2)
                .orElse("");
        } else if (e.getCause() instanceof InvalidFormatException) {
            fields = ((InvalidFormatException) e.getCause()).getPath().stream()
                .map(Reference::getFieldName)
                .reduce((s1, s2) -> s1 + "." + s2)
                .orElse("");
        }
        return new ErrorResponse(
            "Неправильно введенны поля! " + fields,
            LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler({ResourceAlreadyExistException.class, ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleResourceException(RuntimeException e) {
        return new ErrorResponse(
            LocalDateTime.now().toString(),
            e.getMessage()
        );
    }

    @ExceptionHandler({Exception.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(Exception e) {
        e.printStackTrace();
        return new ErrorResponse(
            LocalDateTime.now().toString(),
            "Произошла ошибка! Попробуйте еще раз!"
        );
    }
}