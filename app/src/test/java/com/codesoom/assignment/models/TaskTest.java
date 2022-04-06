package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    private final static Long TASK_ID = 1L;
    private final static String TASK_TITLE = "Task title";

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
    }

    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(TASK_ID);
    }

    @Test
    void setId() {
        Long newId = TASK_ID + 1L;
        task.setId(newId);

        assertThat(task.getId()).isNotEqualTo(TASK_ID);
        assertThat(task.getId()).isEqualTo(newId);
    }

    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void setTitle() {
        String newTitle = "New " + TASK_TITLE;
        task.setTitle(newTitle);

        assertThat(task.getTitle()).isNotEqualTo(TASK_TITLE);
        assertThat(task.getTitle()).isEqualTo(newTitle);
    }
}