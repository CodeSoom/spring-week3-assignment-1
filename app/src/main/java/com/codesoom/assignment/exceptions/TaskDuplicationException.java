package com.codesoom.assignment.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 중복된 할 일을 등록할 때 던집니다.
 */
public class TaskDuplicationException extends CommonException {
    private static final String MESSAGE = "이미 등록된 할 일입니다";

    public TaskDuplicationException() {
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }
}
