package com.codesoom.assignment.exceptions;

public class NotSupportedIdTypeException extends RuntimeException {
    public static final String DEFAULT_MESSAGE_WITH_FORM = "인수 타입은 [%s]여야 합니다. 전달받은 인수 타입: [%s]";

    public NotSupportedIdTypeException(String sourceType, String targetType) {
        super(String.format(DEFAULT_MESSAGE_WITH_FORM, sourceType, targetType));
    }
}
