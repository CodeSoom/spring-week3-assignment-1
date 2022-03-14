package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private Task task;
    private static final String TASK_TITLE = "Test";

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void setId() {
        task.setId(1L);
        assertThat(task.getId()).isEqualTo(1L);
    }

    @Test
    void setTitle() {
        task.setTitle(TASK_TITLE);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

}