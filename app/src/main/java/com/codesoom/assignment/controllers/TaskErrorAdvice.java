package com.codesoom.assignment.controllers;

import com.codesoom.assignment.exceptions.InvalidTaskTitleException;
import com.codesoom.assignment.exceptions.NegativeIdException;
import com.codesoom.assignment.exceptions.NullTaskException;
import com.codesoom.assignment.exceptions.TaskNotFoundException;
import com.codesoom.assignment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskErrorAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ErrorResponse handleNotFound(TaskNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {NegativeIdException.class, NullTaskException.class, InvalidTaskTitleException.class})
    public ErrorResponse handleIllegalArgument(IllegalArgumentException e) {
        return new ErrorResponse(e.getMessage());
    }

}
