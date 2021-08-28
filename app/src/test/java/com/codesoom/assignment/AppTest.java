package com.codesoom.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("App 클래스")
@ExtendWith(MockitoExtension.class)
public final class AppTest {
    @InjectMocks
    private App app;

    @Nested
    @DisplayName("getGreeting 메소드는")
    class Describe_getGreeting {
        @Test
        @DisplayName("인사 메시지를 리턴한다.")
        void it_returns_a_greeting_message() {
            assertThat(app.getGreeting()).isEqualTo("Hello, world!");
        }
    }
}
