package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloControllerTest {

    @Test
    @DisplayName("Hello, World! 유닛 테스트")
    void sayHello() {
        // given
        HelloController helloController = new HelloController();

        // when
        String hello = helloController.sayHello();

        // then
        assertThat(hello).isEqualTo("Hello, World!");
    }
}
