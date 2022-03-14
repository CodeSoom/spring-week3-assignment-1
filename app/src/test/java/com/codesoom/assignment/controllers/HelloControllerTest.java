package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController 클래스")
class HelloControllerTest {

    static final String HELLO_WORLD = "Hello, world!";

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    @Test
    @DisplayName("Hello, world!를 리턴한다.")
    void sayHello() {
        assertThat(helloController.sayHello()).isEqualTo(HELLO_WORLD);
    }
}
