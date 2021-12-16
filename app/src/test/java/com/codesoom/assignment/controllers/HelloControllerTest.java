package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloControllerTest {
    @Test
    void sayHello() {
        HelloController controller = new HelloController();
        String result = controller.sayHello();
        // TODO: result -> Hello, world!
        assertEquals("Hello, world!!!", result);
    }
}