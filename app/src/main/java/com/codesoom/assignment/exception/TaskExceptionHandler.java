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
     * 할 일을 찾지 못한 경우의 예외를 받아 처리합니다.
     */
    @ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "No Such Task Found.")
    @ExceptionHandler(TaskNotFoundException.class)
    public void NoSuchTaskFound() { }
}
