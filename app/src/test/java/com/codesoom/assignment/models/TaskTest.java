package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("할 일 도메인 객체")
class TaskTest {

    @Nested
    @DisplayName("생성자는")
    class Describe_constructor {

        @Nested
        @DisplayName("Title 값이 유효하다면")
        class Context_valid {

            final String validTitle = "TITLE";

            @Test
            @DisplayName("생성을 성공한다.")
            void it_success() {
                Task task = new Task(validTitle);
                assertThat(task.getTitle()).isEqualTo(validTitle);
            }
        }

        @Nested
        @DisplayName("Title 값이")
        class Context_invalid {

            @ParameterizedTest(name = "[{0}] 이면 예외를 던진다.")
            @NullAndEmptySource
            @ValueSource(strings = {" "})
            void it_throw_exception(String givenTitle) {
                assertThatThrownBy(
                        () -> new Task(givenTitle)
                ).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
