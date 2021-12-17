package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskController controller;
    private TaskService taskService;

    private static final String TASK_TITLE = "Test";
    private static final String UPDATE_PREFIX = "Update";

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
    void detail() {
        Task task = controller.detail(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void createNewTask() {
        Task task = new Task();

        int oldSize = controller.list().size();

        task.setTitle(TASK_TITLE);
        controller.create(task);

        int newSize = controller.list().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    void deleteTask() {
        int oldSize = controller.list().size();

        controller.delete(1L);

        int newSize = controller.list().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }

    @Test
    void updateTask() {
        Task source = new Task();
        source.setTitle(UPDATE_PREFIX + TASK_TITLE);

        controller.update(1L, source);

        assertThat(controller.detail(1L).getTitle())
                .isEqualTo(UPDATE_PREFIX + TASK_TITLE);
    }
}