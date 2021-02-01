package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Test
    void list() {
        assertThat(taskController.list()).isEmpty();
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("Test");

        taskController.create(task);

        assertThat(taskController.list()).isNotEmpty();

        assertThat(taskController.list()).hasSize(1);
        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo("Test");

        task.setTitle("Test2");
        taskController.create(task);

        assertThat(taskController.list()).hasSize(2);
        assertThat(taskController.list().get(1).getId()).isEqualTo(2L);
        assertThat(taskController.list().get(1).getTitle()).isEqualTo("Test2");
    }
}