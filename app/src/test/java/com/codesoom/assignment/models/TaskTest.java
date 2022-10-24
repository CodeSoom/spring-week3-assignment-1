package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    private static final Long FIRST_ID = 1L;
    private static final String TASk_TITLE = "task";
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(FIRST_ID);
        task.setTitle(TASk_TITLE);
    }

    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(FIRST_ID);
    }

    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(TASk_TITLE);
    }

    @Test
    void setTitle() {
        final String newTitle = TASk_TITLE + "`";

        task.setTitle(newTitle);

        assertThat(task.getTitle()).isEqualTo(newTitle);
    }

    @Test
    void setId() {
        final Long newId = FIRST_ID + 1L;

        task.setId(newId);

        assertThat(task.getId()).isEqualTo(newId);
    }

}