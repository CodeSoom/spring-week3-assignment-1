package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelloControllerTest {

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    @Test
    void sayHelloTest() {
        assertThat(helloController.sayHello()).isEqualTo("Hello, world!!!");
    }
}