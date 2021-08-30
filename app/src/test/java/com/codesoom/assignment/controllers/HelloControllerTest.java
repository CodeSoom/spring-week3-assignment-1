package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController 클래스")
class HelloControllerTest {

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    @Nested
    @DisplayName("greeting 메소드")
    class Describe_greeting {

        @Test
        @DisplayName("hello를 반환합니다.")
        void it_return_hello() {
            assertThat(helloController.greeting()).isEqualTo("Hello");
        }
    }
}