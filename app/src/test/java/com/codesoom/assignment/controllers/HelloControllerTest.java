package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController 클래스")
class HelloControllerTest {

    @Nested
    @DisplayName("sayHello 메서드는")
    class testSayHello {
        @Nested
        @DisplayName("인자 값 없이 호출되었을 때")
        class sayHelloWithoutParameter {
            @Test
            @DisplayName("문자열을 반환한다.")
            void sayHello() {
                HelloController helloController = new HelloController();
                assertThat(helloController.sayHello()).isEqualTo("Hello, world!");
            }
        }
    }
}