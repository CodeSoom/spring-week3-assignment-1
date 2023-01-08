package com.codesoom.assignment.utils;

import com.codesoom.assignment.exceptions.NegativeIdException;

public class IdValidator {

    public static void validate(Long id) {
        if (id < 0) {
            throw new NegativeIdException(id);
        }
    }
}
