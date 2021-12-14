package com.leverx.dealerstat.exception;

import com.leverx.dealerstat.dto.exception.AnyExceptionDto;
import com.leverx.dealerstat.dto.validation.ValidationErrorResponse;
import com.leverx.dealerstat.dto.validation.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Objects;

@RestControllerAdvice
class ErrorHandlingControllerAdvice {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ValidationErrorResponse onConstraintValidationException(
          ConstraintViolationException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    for (ConstraintViolation violation : e.getConstraintViolations()) {
      error.getViolations().add(
              new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
    }
    return error;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ValidationErrorResponse onMethodArgumentNotValidException(
          MethodArgumentNotValidException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      error.getViolations().add(
              new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
    }
    return error;
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Violation onMethodArgumentTypeMismatchException(
          MethodArgumentTypeMismatchException e) {
    return new Violation(e.getName(), " Should be of type "
            + Objects.requireNonNull(e.getRequiredType()).getName());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  AnyExceptionDto handleServiceError(Exception e) {
    return new AnyExceptionDto(e.getMessage());
  }
}