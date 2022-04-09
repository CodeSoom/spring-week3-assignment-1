package com.codesoom.assignment.exceptions;

public class EmptyTitleException extends RuntimeException {
    public EmptyTitleException() {
        super("title 을 입력해주세요.");
    }
}
