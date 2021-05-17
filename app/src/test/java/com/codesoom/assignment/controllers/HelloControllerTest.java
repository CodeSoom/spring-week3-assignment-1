package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloControllerTest {
    private final String HELLO = "Hello, world!";

    private HelloController controller = new HelloController();

    @Test
    void sayHello() {
        assertThat(controller.sayHello()).isEqualTo(HELLO);
    }
}