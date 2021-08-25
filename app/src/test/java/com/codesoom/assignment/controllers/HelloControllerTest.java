package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HelloController")
public final class HelloControllerTest {
    private final HelloController helloController = new HelloController();

    @Test
    @DisplayName("sayHello() should exist")
    void existTest() {
        assertThat(helloController.sayHello()).isNotEmpty();
    }

    @Test
    @DisplayName("sayHello() should return \"Hello, world!\"")
    void returnTest() {
        assertThat(helloController.sayHello()).isEqualTo("Hello, world!");
    }
}
