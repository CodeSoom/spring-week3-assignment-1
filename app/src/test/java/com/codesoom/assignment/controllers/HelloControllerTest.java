package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class HelloControllerTest {
    @Test
    void sayHello() {
        HelloController controller = new HelloController();

        assertThat(controller.sayHello()).isEqualTo("Hello, World!");
    }

}

