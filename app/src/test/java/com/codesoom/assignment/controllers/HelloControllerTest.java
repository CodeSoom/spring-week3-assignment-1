package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/")
class HelloControllerTest {
    @Test
    @DisplayName("기본 인사를 응답한다.")
    void sayHello() {
        HelloController controller = new HelloController();

        assertThat(controller.sayHello()).isEqualTo("Hello, world!");
    }
}
