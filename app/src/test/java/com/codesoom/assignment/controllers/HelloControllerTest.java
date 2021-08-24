package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController")
class HelloControllerTest {
    @Test
    @DisplayName("문자열을 반환한다.")
    void sayHello() {
        HelloController helloController = new HelloController();
        assertThat(helloController.sayHello()).isEqualTo("Hello, world!");
    }
}