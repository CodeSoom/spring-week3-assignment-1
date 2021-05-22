package com.codesoom.assignment;

public class EmptyTaskTitleException extends RuntimeException {
    public EmptyTaskTitleException() {
        super("Task title is empty.");
    }
}
