package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class HelloControllerTest {

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    @Test
    void sayHello() {
        assertThat(helloController.sayHello()).isEqualTo("Hello, world!");
    }

}
