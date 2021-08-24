package com.codesoom.assignment.exceptions;

public class NotSupportedIdTypeException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "유효하지 않은 타입입니다.";
    public static final String DEFAULT_MESSAGE_WITH_FORM = "인수 타입은 [%s]여야 합니다. 전달받은 인수 타입: [%s]";


    public NotSupportedIdTypeException() {
        super(DEFAULT_MESSAGE);
    }

    public NotSupportedIdTypeException(String message) {
        super(message);
    }

    public NotSupportedIdTypeException(String sourceType, String targetType) {
        super(String.format(DEFAULT_MESSAGE_WITH_FORM, sourceType, targetType));
    }
}
