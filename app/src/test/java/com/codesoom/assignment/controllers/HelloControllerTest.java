package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    static final String HELLO_WORLD = "Hello, world!";

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    @Test
    void sayHello() {
        assertThat(helloController.sayHello()).isEqualTo(HELLO_WORLD);
    }
}
