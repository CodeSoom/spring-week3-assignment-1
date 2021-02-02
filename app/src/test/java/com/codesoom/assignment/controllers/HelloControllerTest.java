package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private HelloController controller;

    @Test
    void sayHello() {
        assertThat(controller.sayHello()).isEqualTo("Hello, World!");
    }

    @Test
    void sayGoodBye() {
        assertThat(controller.sayHello()).isNotEqualTo("Good, Bye!");
    }
}
