package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskController controller;

    @BeforeEach
    void setUp() {
        controller = new TaskController();
    }

    @Test
    void list() {
        assertThat(controller.list()).isEmpty();
    }

    @Test
    void createNewTask() {
        Task task = new Task();

        task.setTitle("test1");
        controller.create(task);

        assertThat(controller.list()).hasSize(1);
        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
        assertThat(controller.list().get(0).getTitle()).isEqualTo("test1");

        task.setTitle("test2");
        controller.create(task);

        assertThat(controller.list()).hasSize(2);
        assertThat(controller.list().get(1).getId()).isEqualTo(2L);
        assertThat(controller.list().get(1).getTitle()).isEqualTo("test2");
    }
}
