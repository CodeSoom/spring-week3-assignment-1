package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    void taskTest() {
        Task task = new Task();
        task.setId(2L);
        task.setTitle("테스트");

        assertThat(task.getId()).isEqualTo(2L);
        assertThat(task.getTitle()).isEqualTo("테스트");
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
