package com.codesoom.assignment.exceptions;

public class NegativeIdException extends RuntimeException {

    private static final String MESSAGE_POSTFIX = ": Id는 0 또는 양수만 허용됩니다.";

    public NegativeIdException(Long id) {
        super(id + MESSAGE_POSTFIX);
    }
}
