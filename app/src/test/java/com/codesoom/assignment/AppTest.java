package com.codesoom.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("App 클래스")
class AppTest {
    App app;

    @BeforeEach
    void setUp() {
        app = new App();
    }

    @Nested
    @DisplayName("getGreeting 메소드는")
    class Describe_getGreeting {

        @Test
        @DisplayName("문자열을 리턴합니다")
        void it_returns_String() {
            assertThat(app.getGreeting()).isInstanceOf(String.class);
        }
    }

    @Nested
    @DisplayName("main 메소드는")
    class main {

    }
}
