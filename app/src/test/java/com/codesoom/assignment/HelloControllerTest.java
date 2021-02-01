package com.codesoom.assignment;

import com.codesoom.assignment.controllers.HelloController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloControllerTest {
    @Test
    void sayHello() {
        assertThat(new HelloController().sayHello()).isEqualTo("Hello, world!");
    }
}