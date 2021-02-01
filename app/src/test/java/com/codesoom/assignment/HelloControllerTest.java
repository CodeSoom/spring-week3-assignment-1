package com.codesoom.assignment;

import com.codesoom.assignment.controllers.HelloController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {
    @Test
    void sayHello() {
        assertThat(new HelloController().sayHello()).isEqualTo("Hello, world!");
    }
}