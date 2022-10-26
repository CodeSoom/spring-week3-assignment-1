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
        task = new Task(FIRST_ID, TASk_TITLE);
    }

    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(FIRST_ID);
    }

    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(TASk_TITLE);
    }

}
