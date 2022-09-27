package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    @Test
    void createTask() {
        Task task = new Task(1L, "test1");
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("test1");
    }
}