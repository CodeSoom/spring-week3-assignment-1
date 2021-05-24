package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController class")
class HelloControllerTest {
    private final String HELLO = "Hello, world!";

    private HelloController controller = new HelloController();

    @Nested
    @DisplayName("sayHello method")
    class DescribeSayHello {
        @Test
        @DisplayName("It returns the greeting \"Hello, world!\"")
        void sayHello() {
            assertThat(controller.sayHello()).isEqualTo(HELLO);
        }
    }
}
