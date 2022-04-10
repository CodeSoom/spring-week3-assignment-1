package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스")
class TaskTest {
    private final Long TASK_ID = 1L;
    private final String TASK_TITLE = "일찍 자기";
    private final Long UPDATE_TASK_ID = 10L;
    private final String UPDATE_TASK_TITLE = "잠은 자기";

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
    }

    @Test
    @DisplayName("id를 반환합니다")
    void getId() {
        assertThat(task.getId()).isEqualTo(TASK_ID);
    }

    @Test
    @DisplayName("타이틀을 반환합니다")
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("id를 작성합니다")
    void setId() {
        task.setId(UPDATE_TASK_ID);
        assertThat(task.getId()).isEqualTo(UPDATE_TASK_ID);
    }

    @Test
    @DisplayName("타이틀을 작성합니다")
    void setTitle() {
        task.setTitle(UPDATE_TASK_TITLE);
        assertThat(task.getTitle()).isEqualTo(UPDATE_TASK_TITLE);
    }
}
