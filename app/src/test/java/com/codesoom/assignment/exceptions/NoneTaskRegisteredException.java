package com.codesoom.assignment.exceptions;

public class NoneTaskRegisteredException extends RuntimeException {

    public NoneTaskRegisteredException() {
        super("등록된 task가 없습니다.");
    }
}
