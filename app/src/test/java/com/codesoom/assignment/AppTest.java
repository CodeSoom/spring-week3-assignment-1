package com.codesoom.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("App 클래스")
public final class AppTest {
    private final App app = new App();

    @Nested
    @DisplayName("getGreeting 메소드는")
    class Describe_getGreeting {
        @Nested
        @DisplayName("언제나")
        class Context_as_always {
            @Test
            @DisplayName("인사 메시지를 리턴한다.")
            void it_returns_a_greeting_message() {
                assertThat(app.getGreeting()).isEqualTo("Hello, world!");
            }
        }
    }
}
