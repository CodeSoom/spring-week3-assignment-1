package com.codesoom.assignment.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class HelloControllerTest {

    @Test
    void sayHello() {
        HelloController controller = new HelloController();
        Assertions.assertThat(controller.sayHello()).isEqualTo("Hello, World!");
    }
}
