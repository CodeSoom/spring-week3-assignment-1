package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloControllerTest {

    @Test
    void sayHello() {
        HelloController helloController = new HelloController();
        String result = helloController.sayHello();

        assertEquals("Hello, world", result);
        assertThat(result).isEqualTo("Hello, world");
    }

}
