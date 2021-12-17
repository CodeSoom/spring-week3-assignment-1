package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HelloController 클래스")
class HelloControllerTest {

    @Nested
    @DisplayName("sayHello 메소드는")
    class Describe_sayHello {
        private HelloController helloController = new HelloController();

        @Test
        @DisplayName("서버의 정상 작동을 확인시켜주는 메시지를 리턴한다.")
        void it_returns_a_message_that_check_server_operation(){
            assertThat(helloController.sayHello()).isNotNull();
        }
    }
}