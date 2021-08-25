package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HelloController 클래스")
public final class HelloControllerTest {
    private final HelloController helloController = new HelloController();

    @Nested
    @DisplayName("sayHello 메소드는")
    class Describe_sayHello {
        @Nested
        @DisplayName("언제나")
        class Context_as_always {
            @Test
            @DisplayName("서버의 정상 작동을 확인시켜주는 메시지를 리턴한다.")
            void it_returns_a_message_checking_the_server_operation() {
                assertThat(helloController.sayHello()).isEqualTo("Hello, world!");
            }
        }
    }
}
