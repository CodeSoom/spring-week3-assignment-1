package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private static final String TASK_TITLE = "test1";
    private static final Long TASK_ID = 1L;
    private static final String NEW_TASK_TITLE = "test2";
    private static final Long NEW_TASK_ID = 2L;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle(TASK_TITLE);
    }

    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(TASK_ID);
    }

    @Test
    void setId() {
        task.setId(NEW_TASK_ID);
        assertThat(task.getId()).isEqualTo(NEW_TASK_ID);
    }

    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void setTitle() {
        task.setTitle(NEW_TASK_TITLE);
        assertThat(task.getTitle()).isEqualTo(NEW_TASK_TITLE);
    }
}
