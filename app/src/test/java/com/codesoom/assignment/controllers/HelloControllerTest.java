package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloControllerTest {
    private HelloController controller;

    @BeforeEach
    void setUp() {
        controller = new HelloController();
    }

    @Test
    void sayHello() {
        String result = controller.sayHello();

        assertThat(result).isEqualTo("Hello, world!");
    }
}
