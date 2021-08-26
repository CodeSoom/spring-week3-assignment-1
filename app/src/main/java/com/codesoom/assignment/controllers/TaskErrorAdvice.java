package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 할 일 관련 에러 처리를 담당합니다.
 */
@ControllerAdvice
public class TaskErrorAdvice {

    public static String ERROR_MESSAGE = "Task not found";

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ErrorResponse handleNotFound() {
        return new ErrorResponse(ERROR_MESSAGE);
    }
}
