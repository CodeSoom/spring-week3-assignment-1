package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void getId() {
        assertThat(task.getId()).isNull();
    }

    @Test
    void setId() {
        Long expected = 3L;
        task.setId(expected);
        assertThat(task.getId()).isEqualTo(expected);
    }

    @Test
    void getTitle() {
        assertThat(task.getTitle()).isNull();
    }

    @Test
    void setTitle() {
        String expected = "test title";
        task.setTitle(expected);
        assertThat(task.getTitle()).isEqualTo(expected);
    }
}