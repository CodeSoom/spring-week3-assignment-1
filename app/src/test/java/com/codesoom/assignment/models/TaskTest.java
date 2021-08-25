package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 모델 테스트")
class TaskTest {

    public static final String title = "test";

    @DisplayName("할 일을 생성한다.")
    @Test
    void create() {
        Task task = new Task(1L, title);

        assertThat(task.getTitle()).isEqualTo(title);
        assertThat(task.getId()).isEqualTo(1L);
    }

    @DisplayName("Task 객체는 동등성 비교를 한다.")
    @Test
    void equivalenceTask() {
        final Task source = new Task(1L, title);
        final Task target = new Task(1L, title);

        assertThat(source).isEqualTo(target);
    }
}
