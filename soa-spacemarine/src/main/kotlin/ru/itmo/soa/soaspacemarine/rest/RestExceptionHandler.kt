package ru.itmo.soa.soaspacemarine.rest

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import jakarta.validation.ConstraintViolationException
import org.apache.logging.log4j.util.Strings
import org.hibernate.validator.internal.engine.path.PathImpl
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import ru.itmo.soa.soaspacemarine.rest.data.ErrorResponse
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ErrorResponse {
        System.err.println(e.message)

        val fields = e.bindingResult.fieldErrors

        return ErrorResponse(
            errorMessage = "Неправильно введенны поля! ${fields.joinToString { it.field }}",
            timestamp = LocalDateTime.now().toString()
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(e: ConstraintViolationException): ErrorResponse {
        System.err.println(e.message)
        val response = ErrorResponse(
            errorMessage = "Неправильно введенны поля! ${
                e.constraintViolations.joinToString {
                    (it.propertyPath as PathImpl).leafNode.name
                }
            }}",
            timestamp = LocalDateTime.now().toString()
        )
        return response
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ErrorResponse {
        System.err.println(e.message)
        val fields = if (e.rootCause is MissingKotlinParameterException) {
            (e.rootCause as MissingKotlinParameterException).path.joinToString(".") { it.fieldName }
        } else if (e.cause is InvalidFormatException) {
            (e.cause as InvalidFormatException).path.joinToString(".") { it.fieldName }
        } else Strings.EMPTY

        val response = ErrorResponse(
            errorMessage = "Неправильно введенны поля! $fields",
            timestamp = LocalDateTime.now().toString()
        )
        return response
    }


    @ExceptionHandler(Exception::class, MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(e: Exception): ErrorResponse {
        System.err.println(e.message)
        return ErrorResponse(
            timestamp = LocalDateTime.now().toString(),
            errorMessage = "Произошла ошибка! Попробуйте еще раз!"
        )
    }

}