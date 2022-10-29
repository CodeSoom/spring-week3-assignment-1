package com.codesoom.assignment.exceptions;

public class NullTaskDtoException extends IllegalArgumentException {
    public NullTaskDtoException() {
        super("null인 객체는 허용되지 않습니다.");
    }
}
