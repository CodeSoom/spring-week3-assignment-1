package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("HelloController 클래스")
class HelloControllerTest {

    @Nested
    @DisplayName("sayHello API는")
    class Describe_of {
        private final HelloController controller = new HelloController();
        private final String result = controller.sayHello();

        @Nested
        @DisplayName("GET 요청을 했다면")
        class Context_with_real {
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
