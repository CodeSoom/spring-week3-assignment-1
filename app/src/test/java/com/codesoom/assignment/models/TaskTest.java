package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTest {

    private final String TEST_TITLE = "TEST_TITLE";
    private final String UPDATE_TITLE = "UPDATE_TITLE";
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task(1L, TEST_TITLE);
    }

    @Test
    void checkGetId() {
        assertThat(task.getId()).isEqualTo(1L);
    }

    @Test
    void checkGetTitle() {
        assertThat(task.getTitle()).isEqualTo(TEST_TITLE);
    }

    @Test
    void checkUpdateTitle() {
        task.updateTitle(UPDATE_TITLE);
        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
    }




}
