package com.codesoom.assignment.repository;

/**
 * 식별자를 생성하는 역할을 가지고 있습니다.
 * 쓰레드 세이프한 식별자를 제공할 책임이 있습니다.
 */
public interface IdGenerator {
    /**
     * 식별자를 생성해 리턴합니다.
     *
     * @return 식별자
     */
    Object generate();
}
