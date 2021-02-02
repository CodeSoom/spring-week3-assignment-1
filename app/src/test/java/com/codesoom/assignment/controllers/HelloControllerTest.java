package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class HelloControllerTest {
    private HelloController controller;

    @BeforeEach
    void setUp() {
        controller = new HelloController();
    }

    @Test
    void sayHello() {
        assertThat(controller.sayHello()).isEqualTo("Hello, World!");
    }
}
