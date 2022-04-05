package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {
    private HelloController controller;

    @BeforeEach
    void setUp() {
        this.controller = new HelloController();
    }

    @Test
    void sayHello() {
        // given
        String result = controller.sayHello();

        // then
        assertEquals("Hello, world!", result);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).isNotBlank();
        assertThat(result).isEqualTo("Hello, world!");

    }

}