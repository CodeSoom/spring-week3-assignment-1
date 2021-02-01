package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;

class TaskControllerTest {

    TaskService taskService;
    TaskController controller;

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
        task.setTitle("Test");

        controller.create(task);
        assertThat(controller.list()).hasSize(1);
        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
        controller.create(task);
        assertThat(controller.list()).hasSize(2);
        assertThat(controller.list().get(1).getId()).isEqualTo(2L);
    }

}
