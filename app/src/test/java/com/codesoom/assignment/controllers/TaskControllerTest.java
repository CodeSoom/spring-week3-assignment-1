package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class TaskControllerTest {
    private TaskController controller;
    private TaskService taskService;

    private static final String TASK_TITLE = "test";
    private static final String UPDATE_POSTFIX = "New";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        controller.create(task);
    }

    @Test
    void list() {
        assertThat(controller.list()).isNotEmpty();
    }

    @Test
    void detailWithValidId() {
        assertThat(controller.detail(1L).getId()).isEqualTo(1L);
        assertThat(controller.detail(1L).getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void detailWithInvalidId() {
        assertThatThrownBy(() -> controller.detail(0L)).isInstanceOf(TaskNotFoundException.class);
    }
}
