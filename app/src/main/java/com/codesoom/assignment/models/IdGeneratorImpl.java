package com.codesoom.assignment.models;

import org.springframework.stereotype.Component;

@Component
public class IdGeneratorImpl implements IdGenerator {
    private static Long sequence = 1L;

    public synchronized Long nextSequence() {
        return sequence++;
    }
}
