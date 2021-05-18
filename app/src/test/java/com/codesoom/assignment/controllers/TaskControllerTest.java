package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    private final Long ID = 1L;
    private final String TITLE = "SAMPLE TITLE";

    private TaskController controller;
    private TaskService taskService;
    private Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        task = new Task();
        task.setId(ID);
        task.setTitle(TITLE);

        taskService.createTask(task);
    }

    @Test
    void tasks() {
        assertThat(controller.list()).isEmpty();
    }

    @Test
    void task() {
        Task task = controller.detail(ID);

        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(ID);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("Test");
        controller.create(task);

        task.setTitle("Test2");
        controller.create(task);

        assertThat(controller.list()).hasSize(2);

        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
        assertThat(controller.list().get(0).getTitle()).isEqualTo("Test");

        assertThat(controller.list().get(1).getId()).isEqualTo(2L);
        assertThat(controller.list().get(1).getTitle()).isEqualTo("Test2");
    }
}