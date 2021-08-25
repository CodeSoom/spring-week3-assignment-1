package com.codesoom.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 할 일과 관계된 예외를 처리하는 핸들러입니다.
 */
@ControllerAdvice
public class TaskExceptionHandler {

    /**
     * 조회된 할 일이 없는 경우 예외를 던집니다.
     */
    @ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "No Such Task Found.")
    @ExceptionHandler(TaskNotFoundException.class)
    public void NoSuchTaskFound() { }
}
