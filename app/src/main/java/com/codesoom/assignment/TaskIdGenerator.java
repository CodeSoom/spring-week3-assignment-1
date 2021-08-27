package com.codesoom.assignment;

import org.springframework.stereotype.Component;

@Component
public class TaskIdGenerator implements IdGenerator {

    private Long lastId = 0L;

    @Override
    public long generate() {
        increaseLastId();
        return lastId;
    }

    private synchronized void increaseLastId() {
        lastId++;
    }
}
