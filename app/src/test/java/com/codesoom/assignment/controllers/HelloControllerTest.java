package com.codesoom.assignment.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Hello Controller Class")
class HelloControllerTest {

    HelloController helloController = new HelloController();
    @Nested
    @DisplayName("Say Hello method")
    class Describe_say_hello {

        private String hello = "Hello, world!!!";
        @Nested
        @DisplayName("when accessing to /")
        class Context_default {
            @Test
            @DisplayName("returns Hello, world!!!")
            void it_returns_a_valid_complex() {
                String result = helloController.sayHello();
                Assertions.assertThat(result).isEqualTo("Hello, world!!!");
            }
        }
    }
}

