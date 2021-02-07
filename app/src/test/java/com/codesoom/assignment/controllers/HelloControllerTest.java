package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloControllerTest {
    @Test
    void testHello() {
        HelloController helloController = new HelloController();

        assertEquals(helloController.hello(), "Hello World!");
        assertThat(helloController.hello()).isEqualTo("Hello World!");
    }
}
