package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HelloController 클래스")
public final class HelloControllerTest {
    private HelloController helloController = null;

    @Nested
    @DisplayName("sayHello 메소드는")
    class Describe_sayHello {
        @Nested
        @DisplayName("선조건")
        class Context_precondition {
            @Nested
            @DisplayName("HelloController가 null인 환경에서")
            class Context_helloController_null {
                @Test
                @DisplayName("NullPointerException을 던진다.")
                void it_throw_a_nullPointException() {
                    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> helloController.sayHello());
                }
            }
        }

        @Nested
        @DisplayName("올바른 사용자 시나리오(happy path) 환경에서")
        class Context_happyPath {
            public Context_happyPath() {
                helloController = new HelloController();
            }
    
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
}
