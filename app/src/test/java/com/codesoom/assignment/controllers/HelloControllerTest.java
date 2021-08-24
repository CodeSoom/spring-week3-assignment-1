package com.codesoom.assignment.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Hello Controller는")
class HelloControllerTest {

    private HelloController helloController = new HelloController();

    @Test
    @DisplayName("인사를 하면 인사말을 건내준다.")
    void sayHello() {
        Assertions.assertThat(helloController.sayHello()).isEqualTo("Hello, world!");
    }
}