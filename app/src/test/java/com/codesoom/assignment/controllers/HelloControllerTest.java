package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {
    @Test
    void sayHello() {
        HelloController controller = new HelloController();
        String result = controller.sayHello();
        assertEquals("Hello, world!", result);
        // TODO: result -> Hello, world!
    }
}