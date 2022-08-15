package com.codesoom.assignment.repository;

import java.util.UUID;

public class UUIdGenerator implements IdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
