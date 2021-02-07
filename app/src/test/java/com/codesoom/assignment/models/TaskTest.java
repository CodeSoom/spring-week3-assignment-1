package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    @Test
    void createTask() {
        Task task = new Task();
        task.setTitle("Play Music");
        task.setId(1L);

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("Play Music");
    }

    @Test
    void createTaskWithNegativeNumberId() {
        Task task = new Task();
        task.setTitle("Play Music");
        task.setId(-1L);

        assertThat(task.getId()).isEqualTo(-1L);
        assertThat(task.getTitle()).isEqualTo("Play Music");
    }
}