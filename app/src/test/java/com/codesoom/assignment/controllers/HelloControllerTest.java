package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("HelloController 에서")
class HelloControllerTest {

    @Nested
    @DisplayName("sayHello 메소드는")
    class Describe_of {
        private final HelloController controller = new HelloController();

        @Nested
        @DisplayName("정상적인 호출이 되었을 경우")
        class Context_with_call {
            private final String result = controller.sayHello();

            @Test
            @DisplayName("문자열을 반환한다.")
            void it_returns_string_with_statusCode() {
                assertThat(result).isNotNull();
                assertThat(result).isNotEmpty();
                assertThat(result).isNotBlank();
                assertThat(result).isEqualTo("Hello, world!");
            }
        }
    }
}
