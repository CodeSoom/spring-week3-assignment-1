package com.codesoom.assignment;

public class TitleAlreadyExistException extends RuntimeException {
    public TitleAlreadyExistException(String title) {
        super("title already exist: " + title);
    }
}
