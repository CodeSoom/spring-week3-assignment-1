package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloControllerTest {
    private static final String HELLO = "Hello, world!";

    /**
     * Test about "GET /" request.
     * Check if hello text is expected tesst
     */
    @Test
    void sayHello() {
        assertThat(new HelloController().sayHello()).isEqualTo(HELLO);
    }
}
