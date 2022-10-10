package com.codesoom.assignment.controllers;

import com.codesoom.assignment.appllication.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    @Autowired
    private HelloController controller;

    @BeforeEach
    void setUp() {
        controller = new HelloController();
    }

    @Test
    void sayHello() {
        //TODO: result -> Hello, world!
        assertEquals("Hello, world!", controller.sayHello());
        assertThat(controller.sayHello()).isEqualTo("Hello, world!");
    }


}