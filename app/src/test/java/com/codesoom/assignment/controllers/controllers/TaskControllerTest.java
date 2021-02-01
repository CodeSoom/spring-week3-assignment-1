package com.codesoom.assignment.controllers.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.controllers.TaskController;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);
    }

    @Test
    void list() {
        assertThat(controller.list()).isEmpty();
    }

    @Test
    void createNewTask() {

        Task task = new Task();
        task.setTitle("Test1");
        controller.create(task);

        assertThat(controller.list()).isNotEmpty();
        assertThat(controller.list()).hasSize(1);
    }
}