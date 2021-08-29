package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * TaskController에서 발생한 예외처리를 수행한다.
 */
@ControllerAdvice
public class TaskErrorAdvice {
    /**
     * TaskNotFoundException이 발생한 경우
     * 에러메시지("Task not found")를 리턴한다.
     *
     * @return 에러메시지("Task not found")
     * @see TaskController#detail(Long) detail
     * @see TaskController#update(Long, com.codesoom.assignment.models.Task) update
     * @see TaskController#patch(Long, com.codesoom.assignment.models.Task) patch
     * @see TaskController#delete(Long) delete
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ErrorResponse handleNotFound() {
        return new ErrorResponse("Task not found");
    }
}
