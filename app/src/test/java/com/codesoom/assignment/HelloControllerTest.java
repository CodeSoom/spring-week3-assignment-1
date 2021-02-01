package com.codesoom.assignment;

import com.codesoom.assignment.controllers.HelloController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {
    @Test
    void sayHello() {
        assertEquals("Hello, world!", new HelloController().sayHello());
    }
}