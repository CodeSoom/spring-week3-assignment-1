package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController 클래스의")
class HelloControllerTest {

    @Nested
    @DisplayName("sayHello 메소드는")
    class Describe_of_sayHello {

        @Nested
        @DisplayName("호출 했을 때")
        class Context_with_call {

            @Test
            @DisplayName("'Hello, world!' 문자열을 반환한다")
            void it_returns_valid_string() {
                HelloController controller = new HelloController();
                String test = controller.sayHello();
                assertThat(test)
                        .isEqualTo("Hello, world!");
            }
        }
    }
}
