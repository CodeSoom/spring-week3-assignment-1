package com.codesoom.assignment;

import org.springframework.stereotype.Component;

@Component
public class TaskIdGenerator {

    private Long lastId = 0L;

    public long generate() {
        increaseLastId();
        return lastId;
    }

    private synchronized void increaseLastId() {
        lastId++;
    }
}
