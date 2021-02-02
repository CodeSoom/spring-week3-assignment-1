package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {
    private static final String TASK_TITLE = "test";

    private TaskController controller;

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        controller = new TaskController(taskService);
    }

    @Test
    void list() {
        assertThat(controller.list()).isEmpty();
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        controller.create(task);

        assertThat(controller.list()).isNotEmpty();
    }

    @Test
    void getTaskWithValidId() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        controller.create(task);

        assertThat(controller.detail(1L).getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> controller.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
