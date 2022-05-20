package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HelloControllerTest 클래스")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HelloControllerTest {
    @Nested
    class sayHello_메서드는 {
        private HelloController helloController;

        @BeforeEach
        void setUp() {
            helloController = new HelloController();
        }

        @Test
        void Hello_world라는_문자열을_반환한다() {
            assertThat(helloController.sayHello()).isEqualTo("Hello, world!");
        }
    }
}
