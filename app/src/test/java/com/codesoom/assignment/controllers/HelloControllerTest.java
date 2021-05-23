package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("HelloController")
class HelloControllerTest {
    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = mock(HelloController.class);
    }

    @Nested
    @DisplayName("sayHello() 메서드는")
    class describe_sayhello {
        @Test
        @DisplayName("호출시 특정 메시지를 리턴해준다")
        void sayhello_response() {
            //given
            given(helloController.sayHello())
                    .willReturn("Hello, World");

            //when
            String message = helloController.sayHello();

            //then
            assertThat(message).isEqualTo("Hello, World");
        }
    }
}
