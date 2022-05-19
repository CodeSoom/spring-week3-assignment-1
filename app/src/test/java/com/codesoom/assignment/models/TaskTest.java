package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 모델")
class TaskTest {
    static final Long TASK_ID = 1L;
    static final String TASK_TITLE = "test";

    @Test
    void create() {
        Task task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);

        assertThat(task.getId()).isEqualTo(TASK_ID);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }
}
