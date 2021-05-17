package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloControllerTest {

    @Test
    void sayHello() {
        HelloController controller = new HelloController();
        String test = controller.sayHello();
        assertThat(test)
                .isEqualTo("Hello, world!");
    }
}
