package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloControllerTest {
    @Test
    void testSayHello() {
        HelloController helloController = new HelloController();
        //AssertJ
        assertThat(helloController.hello()).isEqualTo("Hello World!");
        //JUnit5
        assertEquals(helloController.hello(), "Hello World!");
    }
}
