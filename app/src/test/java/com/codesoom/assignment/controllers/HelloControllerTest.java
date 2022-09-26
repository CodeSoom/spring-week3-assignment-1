package com.codesoom.assignment.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class HelloControllerTest {

    @Test
    void sayHello() {
        HelloController helloController = new HelloController();
        String result = helloController.sayHello();
        Assertions.assertThat(result).isEqualTo("Hello, world!!!");
    }

}
