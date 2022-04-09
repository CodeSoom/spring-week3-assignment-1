package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private static final String TASK_TITLE = "과제하기";
    private static final String UPDATE_POSTFIX = "!";
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle(TASK_TITLE);
    }

    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(1L);
    }

    @Test
    void setId() {
        task.setId(2L);
        assertThat(task.getId()).isEqualTo(2L);
    }

    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void setTitle() {
        task.setTitle(TASK_TITLE + UPDATE_POSTFIX);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
    }
}