package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private Task task;
    private static final String TASK_TITLE = "gigi";
    private static final Long TASK_ID = 1L;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
    }

    @Test
    void getId() {
        Long id = task.getId();

        assertThat(id).isEqualTo(TASK_ID);
    }

    @Test
    void setId() {
        Long source = 2L;

        task.setId(source);

        assertThat(task.getId()).isEqualTo(source);
    }

    @Test
    void getTitle() {
        String title = task.getTitle();

        assertThat(title).isEqualTo(TASK_TITLE);
    }

    @Test
    void setTitle() {
        String title = "masdm";

        task.setTitle(title);

        assertThat(task.getTitle()).isEqualTo(title);
    }
}