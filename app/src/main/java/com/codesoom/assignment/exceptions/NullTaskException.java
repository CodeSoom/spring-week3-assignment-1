package com.codesoom.assignment.exceptions;

public class NullTaskException extends RuntimeException {
    public NullTaskException() {
        super("null인 Task 객체는 허용되지 않습니다.");
    }
}
