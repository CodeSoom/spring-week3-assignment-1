package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    @Test
    void sayHello() {
        //assertEquals(helloController.sayHello(),"hello, world!");
        assertEquals(helloController.sayHello(),"Hello, world!");

    }
}
