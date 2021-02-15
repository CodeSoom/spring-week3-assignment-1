package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HelloControllerTest {
    @Test
    void testAssertJ(){
        String name = "weno";
        assertThat(name).isEqualTo("weno");
    }

    @Test
    void testGetHello(){
        HelloController helloController = new HelloController();
        String hello = helloController.getHello();

        assertThat(hello).isEqualTo("hello");
        assertThat(hello).isNotEqualTo("hello!");
        assertThat(hello).isEqualToIgnoringCase("Hello");
    }

}
