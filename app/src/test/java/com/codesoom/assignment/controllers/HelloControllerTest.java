package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class HelloControllerTest {
    @Test
    void  sayHello(){
        HelloController controller = new HelloController();
        //assertEquals("Hello, world!!!", result);
        //assertJ가 더 편하다.
        assertThat(controller.sayHello()).isEqualTo("Hello, world!!!");
    }
}