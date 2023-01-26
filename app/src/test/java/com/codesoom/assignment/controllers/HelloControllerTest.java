package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {
    HelloController controller

    @BeforeEach
    void setUP() {
        controller = new HelloController();
    }

    @Test
    void sayHello() {
        String result = controller.sayHello();

        assertThat(result).isEqualTo("Hello, World!");
    }
}