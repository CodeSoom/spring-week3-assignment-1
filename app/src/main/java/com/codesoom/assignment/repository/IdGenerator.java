package com.codesoom.assignment.repository;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 식별자를 생성하는 역할을 가지고 있습니다.
 * 쓰레드 세이프한 식별자를 제공할 책임이 있습니다.
 */
public class IdGenerator {
    private final AtomicLong id = new AtomicLong();

    /**
     * 식별자를 리턴하고 값을 증가시킵니다.
     *
     * @return 식별자
     */
    public Long generate() {
        return id.getAndIncrement();
    }
}
