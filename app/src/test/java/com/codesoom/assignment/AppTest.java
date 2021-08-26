package com.codesoom.assignment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("App 클래스")
public final class AppTest {
    private App app = null;

    @Nested
    @DisplayName("getGreeting 메소드는")
    class Describe_getGreeting {
        @Nested
        @DisplayName("선조건")
        class Context_precondition {
            @Nested
            @DisplayName("'null 포인터를 참조하는 개체는 메소드를 호출할 수 없다.'를 위반한 경우")
            class Context_app_null {
                @Test
                @DisplayName("NullPointerException을 던진다.")
                void it_throw_a_nullPointException() {
                    assertThatExceptionOfType(NullPointerException.class).isThrownBy((() -> app.getGreeting()));
                }
            }
        }

        @Nested
        @DisplayName("올바른 사용자 시나리오(happy path)에서")
        class Context_happyPath {
            public Context_happyPath() {
                app = new App();
            }

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
}
