package com.codesoom.assignment.common.aop;

import com.codesoom.assignment.common.exceptions.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 할 일과 관련된 예외의 처리를 담당합니다.
 */
@RestControllerAdvice
public class TaskExceptionHandler {

    private Logger log = LoggerFactory.getLogger(TaskExceptionHandler.class);

    // taskId에 맞는 할 일을 못찾은 경우 던져진 예외를 처리합니다.
    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleTaskNotFoundException(TaskNotFoundException ex){
        return ex.getMessage();
    }

}

