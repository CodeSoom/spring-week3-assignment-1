package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController 클래스")
class HelloControllerTest {
    @DisplayName("sayHello 메서드는")
    @Nested
    class Describe_sayHello {
        private HelloController helloController;

        @BeforeEach
        void prepareHelloController() {
            helloController = new HelloController();
        }

        String subject() {
            return helloController.sayHello();
        }

        @DisplayName("서버 생존여부 확인용 문자열을 리턴한다.")
        @Test
        void it_returns_string() {
            final String result = subject();
            assertThat(result).isInstanceOf(String.class);
        }

    }
}
