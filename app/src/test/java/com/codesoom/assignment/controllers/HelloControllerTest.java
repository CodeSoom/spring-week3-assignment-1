package com.codesoom.assignment.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HelloController 클래스")
class HelloControllerTest {
    HelloController helloController = new HelloController();

    @Nested
    @DisplayName("sayHello 메서드는")
    class Describe_sayHello {
        private final String helloWorld = "Hello, world!";

        @Test
        @DisplayName(helloWorld + " 문자열을 리턴한다.")
        void it_returns_string() {
            Assertions.assertThat(helloController.sayHello()).isEqualTo(helloWorld);
        }
    }
}