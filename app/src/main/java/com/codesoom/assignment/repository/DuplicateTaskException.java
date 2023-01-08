package com.codesoom.assignment.repository;

public class DuplicateTaskException extends IllegalArgumentException {
    public DuplicateTaskException(String title) {
        super(title + ": 이미 존재하는 task입니다.");
    }
}
