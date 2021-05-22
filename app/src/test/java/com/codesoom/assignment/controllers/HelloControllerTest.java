package com.codesoom.assignment.controllers;

import com.codesoom.assignment.common.exceptions.TaskNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HelloController의 단위 테스트")
class HelloControllerTest {

    @Nested
    @DisplayName("sayHello 메소드는")
    class Describe_sayHello {

        @Nested
        @DisplayName("만약 메소드를 호출한다면")
        class Context_call_method {

            private HelloController helloController = new HelloController();

            private String helloMessage = "Hello, world!";

            @Test
            @DisplayName("'Hello, world!' 메세지를 반환합니다.")
            void it_return_hello_message(){
                Assertions.assertThat(helloController.sayHello()).isEqualTo(helloMessage);
            }

        }

    }

}
