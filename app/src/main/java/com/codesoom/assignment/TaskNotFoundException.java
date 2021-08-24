package com.codesoom.assignment;

/**
 * 할 일이 없는 경우에 대한 예외입니다.
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Task not found: " + id);
    }
}
