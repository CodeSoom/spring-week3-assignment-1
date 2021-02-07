package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private static final Long TASK_ID = 1L;
    private static final String TASK_TITLE = "Test";

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void handleId() {
        task.setId(TASK_ID);
        assertThat(task.getId()).isEqualTo(TASK_ID);
    }

    @Test
    void handleTitle() {
        task.setTitle(TASK_TITLE);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }
}
