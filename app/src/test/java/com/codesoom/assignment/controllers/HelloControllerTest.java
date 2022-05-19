package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloController 클래스")
class HelloControllerTest {

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class sayHello_메서드는 {
        static final String HELLO_FRIEND = "Hello, Friend!";

        private HelloController helloController;

        @BeforeEach
        void setUp() {
            helloController = new HelloController();
        }

        @Test
        @DisplayName("Hello, Friend! 를 리턴한다.")
        void Hello_Friend_를_리턴한다() {
            assertThat(helloController.sayHello()).isEqualTo(HELLO_FRIEND);
        }
    }
}
