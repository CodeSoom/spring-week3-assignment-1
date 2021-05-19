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

    @Test
    void detail() {
        Task task = new Task();
        task.setTitle("task test");
        controller.create(task);
        assertThat(controller.detail(1L).getTitle()).isEqualTo(task.getTitle());
        assertThatThrownBy(() -> controller.detail(100L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void update() {
        Task src = new Task();
        src.setTitle("task test");
        controller.create(src);
        assertThat(controller.detail(1L).getTitle()).isEqualTo(src.getTitle());

        Task task = new Task();
        task.setTitle("updated task");
        assertThat(controller.update(1L, task).getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    void delete() {
        Task task = new Task();
        task.setTitle("task test");
        controller.create(task);
        assertThat(controller.detail(1L).getTitle()).isEqualTo(task.getTitle());

        controller.delete(1L);
        assertThat(controller.list()).isEmpty();
        assertThatThrownBy(() -> controller.detail(1L)).isInstanceOf(TaskNotFoundException.class);
    }

}
