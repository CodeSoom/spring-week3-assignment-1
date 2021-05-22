package com.codesoom.assignment;

public class TaskEmptyTitleException extends RuntimeException {
    public TaskEmptyTitleException() {
        super("Task title is empty.");
    }
}
