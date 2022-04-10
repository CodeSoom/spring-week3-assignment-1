package com.codesoom.assignment;

public class NotProperTaskFormatException extends RuntimeException {

    public NotProperTaskFormatException(String title) {
        super(String.format("Task format is poor : %s", title));
    }

}
