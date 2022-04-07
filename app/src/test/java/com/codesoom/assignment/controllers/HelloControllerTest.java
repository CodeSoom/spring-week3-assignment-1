package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController 클래스")
class HelloControllerTest {
    private HelloController controller;

    @BeforeEach
    void setUp() {
        controller = new HelloController();
    }

    @Nested
    @DisplayName("sayHello 메서드는")
    class Describe_sayHello {
        @Nested
        @DisplayName("path가 기본 경로로 들어오면")
        class Context_with_default_path {
            @Test
            @DisplayName("환영 메세지를 반환합니다")
            void sayHello() {
                String result = controller.sayHello();

                assertThat(result).isEqualTo("Hello, world!");
            }
        }
    }
}
