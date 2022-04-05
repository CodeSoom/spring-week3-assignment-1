package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private static final String TASK_TITLE = "Title";

    @Test
    void getId() {
        // given
        Task task = new Task();
        task.setId(1L);
        task.setTitle(TASK_TITLE);

        // when
        Long taskId = task.getId();

        // then
        assertThat(taskId).isPositive();
        assertThat(taskId).isEqualTo(1L);
    }

    @Test
    void setId() {
        // given
        Task task = new Task();
        task.setId(1L);
        task.setTitle(TASK_TITLE);

        // when
        task.setId(2L);
        Long taskId = task.getId();

        // then
        assertThat(taskId).isPositive();
        assertThat(taskId).isEqualTo(2L);
    }

    @Test
    void getTitle() {
        // given
        Task task = new Task();
        task.setId(1L);
        task.setTitle(TASK_TITLE);

        // when
        String taskTitle = task.getTitle();

        // then
        assertThat(taskTitle).isNotEmpty();
        assertThat(taskTitle).isNotBlank();
        assertThat(taskTitle).isEqualTo(TASK_TITLE);
    }

    @Test
    void setTitle() {
        // given
        Task task = new Task();
        task.setId(1L);
        task.setTitle(TASK_TITLE);

        // when
        task.setTitle("updateTitle");

        // then
        assertThat(task.getTitle()).isNotEmpty();
        assertThat(task.getTitle()).isNotBlank();
        assertThat(task.getTitle()).isEqualTo("updateTitle");
    }
}