package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    private Task task = new Task();

    @BeforeEach
    void before() {
        task.setId(1L);
        task.setTitle("test");
    }

    @Test
    void setId() {
        Task task = new Task();
        task.setId(1L);

        assertThat(task.getId()).isEqualTo(1L);
    }

    @Test
    void setTitle() {
        Task task = new Task();
        task.setTitle("test");

        assertThat(task.getTitle()).isEqualTo("test");
    }

    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(1L);
    }

    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo("test");
    }
}
