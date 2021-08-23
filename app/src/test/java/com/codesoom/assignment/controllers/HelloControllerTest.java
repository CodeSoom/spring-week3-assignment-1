package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HelloControllerTest {

    @Test
    @DisplayName("인사문을 리턴한다")
    void sayHello() {
        HelloController controller = new HelloController();

        assertThat(controller.sayHello()).isEqualTo("Hello, World!");
    }
}
