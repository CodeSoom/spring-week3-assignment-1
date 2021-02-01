package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void detail() {
        assertThatThrownBy(() -> taskController.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);

        Task task = new Task();
        task.setTitle("Test");
        taskController.create(task);

        assertThat(taskController.detail(1L).getId()).isEqualTo(1L);
        assertThat(taskController.detail(1L).getTitle()).isEqualTo("Test");
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

    @Test
    void updateTask() {
        Task task = new Task();
        task.setTitle("Test");
        taskController.create(task);

        task.setTitle("Modified");
        taskController.update(1L, task);

        assertThat(taskController.detail(1L).getTitle()).isNotEqualTo("Test");
        assertThat(taskController.detail(1L).getTitle()).isEqualTo("Modified");
    }

    @Test
    void patchTask() {
        Task task = new Task();
        task.setTitle("Test");
        taskController.create(task);

        task.setTitle("Modified");
        taskController.patch(1L, task);

        assertThat(taskController.detail(1L).getTitle()).isNotEqualTo("Test");
        assertThat(taskController.detail(1L).getTitle()).isEqualTo("Modified");
    }
}