package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.exceptions.TaskInvalidFormatException;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 *  Controller에서 발생하는 예외들을 처리합니다.
 */
@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    protected ErrorResponse handleNoSuchElementException(TaskNotFoundException e) {
        return new ErrorResponse(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TaskInvalidFormatException.class)
    protected ErrorResponse handleTaskInvalidFormatException(TaskInvalidFormatException e) {
        return new ErrorResponse(e.getCode(), e.getMessage());
    }

}
