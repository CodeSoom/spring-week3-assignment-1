package com.codesoom.assignment.exceptions;

public class NegativeIdException extends IllegalArgumentException {

    public NegativeIdException(Long id) {
        super(id + ": Id는 0 또는 양수만 허용됩니다.");
    }
}
