package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController 클래스")
class HelloControllerTest {
    final HelloController helloController = new HelloController();

    @Nested
    @DisplayName("sayHello 메소드는")
    class Describe_sayHello {
        @Test
        @DisplayName("문자열을 리턴합니다")
        void it_returns_() {
            assertThat(helloController.sayHello()).isInstanceOf(String.class);
        }
    }

}
