package com.codesoom.assignment.exception;

/**
 * 할 일을 찾을 수 없는 경우 던집니다.
 */
public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException(String id) {
        super(String.format("id %s를 할 일 리스트에서 찾을 수 없습니다.", id));
    }
}
