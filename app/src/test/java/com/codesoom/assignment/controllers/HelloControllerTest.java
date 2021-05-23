package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController 클래스의")
class HelloControllerTest {
    private HelloController helloController;

    @BeforeEach
    void prepareHelloController() {
        helloController = new HelloController();
    }

    @Nested
    @DisplayName("sayHello 메서드는")
    class Describe_sayHello {

        @Test
        @DisplayName("인사말을 반환한다")
        void It_returns_greeting() {
            assertThat(helloController.sayHello())
                    .isEqualTo("Hello, world!");
        }
    }
}
