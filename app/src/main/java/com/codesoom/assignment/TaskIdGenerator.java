package com.codesoom.assignment;

import org.springframework.stereotype.Component;

@Component
public class TaskIdGenerator implements IdGenerator<Long> {

    private Long lastId = 0L;

    @Override
    public Long generate() {
        increaseLastId();
        return lastId;
    }

    private synchronized void increaseLastId() {
        lastId++;
    }
}
