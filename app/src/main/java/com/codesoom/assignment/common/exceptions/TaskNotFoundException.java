package com.codesoom.assignment.common.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 할 일을 찾을 수 없는 경우에 던집니다.
 */
public class TaskNotFoundException extends RuntimeException{

    private Logger log = LoggerFactory.getLogger(TaskNotFoundException.class);

    private String message = "해당하는 Task가 존재하지 않습니다.";

    public TaskNotFoundException() {
        log.error(message);
    }

    public String getMessage() {
        return message;
    }

}
