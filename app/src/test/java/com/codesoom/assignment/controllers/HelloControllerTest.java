package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HelloControllerTest {

    @Test
    void hello() {
        HelloController helloController = new HelloController();
        assertThat(helloController.hello()).isEqualTo("Hello, world!");
    }
}
