package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        this.taskService = new TaskService();
        this.controller = new TaskController(this.taskService);
    }

    @Test
    void list() {

        assertThat(controller.list()).isEmpty();
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("");

        controller.create(task);
        assertThat(controller.list()).isNotEmpty();
        assertThat(controller.list()).hasSize(1);

        controller.create(task);
        assertThat(controller.list()).hasSize(2);

        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
    }
}