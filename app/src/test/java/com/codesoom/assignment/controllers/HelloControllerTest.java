package com.codesoom.assignment.controllers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController")
class HelloControllerTest {
    private HelloController helloController;

    @BeforeEach
    void setUp(){
        helloController = new HelloController();
    }

    @Test
    @DisplayName("sayHello()  호출시 Hello, World 리턴")
    void sayHello(){
        assertThat(helloController.sayHello()).isEqualTo("Hello, World");
    }
}
