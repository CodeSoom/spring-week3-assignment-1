package com.codesoom.assignment.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task test")
class TaskTest {

    private Task task;

    @DisplayName("타이틀 유효성 검사 테스트")
    @Nested
    class validate_Title {

        @Test
        @DisplayName("타이틀이 유효한 경우")
        void validateTitle() {
            task = new Task(0L, "helloTest");
            assertThat(task.hasValidTitle()).isTrue();
        }

        @Test
        @DisplayName("타이틀이 유효하지 않은 경우")
        void invalidateTitle() {
            task = new Task();
            assertThat(task.hasValidTitle()).isFalse();
        }
    }
}