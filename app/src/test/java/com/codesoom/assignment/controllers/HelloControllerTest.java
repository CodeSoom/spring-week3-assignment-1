package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * HelloController에 대해 유닛테스트를 실행한다.
 */
class HelloControllerTest {
    @Test
    @DisplayName("문자열은 반환한다.")
    void sayHello() {
        HelloController helloController = new HelloController();
        assertThat(helloController.sayHello()).isEqualTo("Hello, world!");
    }
}