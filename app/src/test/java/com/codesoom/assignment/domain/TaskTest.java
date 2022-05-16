package com.codesoom.assignment.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task test")
class TaskTest {

    private Task task;

    @BeforeEach()
    void setUp() {
        task = new Task();
    }

    @DisplayName("타이틀 유효성 검사 테스트")
    class validate_Title {
        @DisplayName("타이틀이 유효한 경우")
        void validateTitle() {
            assertThat(task.hasValidTitle()).isTrue();
        }

        @DisplayName("타이틀이 유효하지 않은 경우")
        void invalidateTitle() {
            assertThat(task.hasValidTitle()).isFalse();
        }
    }
}