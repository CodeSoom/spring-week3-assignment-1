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

    @BeforeEach
    void setUp() {
        controller = new TaskController(new TaskService());
    }

    void createTestTask(String source) {
        Task task = new Task();
        task.setTitle(source);

        controller.create(task);
    }

    Task createTestFullTask(String source) {
        Task task = new Task();
        task.setTitle(source);

        controller.create(task);
        return task;
    }

    @Test
    void list() {
        assertThat(controller.list()).isEmpty();

        createTestTask("test");
        createTestTask("test2");

        assertThat(controller.list()).hasSize(2);
    }

    @Test
    void getDetail() {
        assertThatThrownBy(() -> controller.detail(1L)).isInstanceOf(TaskNotFoundException.class);

        createTestTask("test");
        createTestTask("test2");

        assertThat(controller.detail(1L).getTitle()).isEqualTo("test");
        assertThat(controller.detail(2L).getTitle()).isEqualTo("test2");
    }

    @Test
    void createNewTask() {
        assertThat(controller.list()).isEmpty();

        createTestTask("test");

        assertThat(controller.list()).isNotEmpty();
        assertThat(controller.list()).hasSize(1);
        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
        assertThatThrownBy(() -> controller.detail(2L)).isInstanceOf(TaskNotFoundException.class);

        createTestTask("test2");

        assertThat(controller.list().get(1).getId()).isEqualTo(2L);
        assertThat(controller.list()).hasSize(2);
    }

    @Test
    void updateTask() {
        createTestTask("test");

        assertThat(controller.detail(1L).getTitle()).isEqualTo("test");

        Task task = new Task();
        task.setTitle("new Test");
        controller.update(1L, task);

        assertThat(controller.detail(1L).getTitle()).isEqualTo("new Test");
        assertThatThrownBy(() -> controller.detail(2L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void patchTask() {
        createTestTask("test");

        assertThat(controller.detail(1L).getTitle()).isEqualTo("test");

        Task task = new Task();
        task.setTitle("new Test");
        controller.update(1L, task);

        assertThat(controller.detail(1L).getTitle()).isEqualTo("new Test");
        assertThatThrownBy(() -> controller.detail(2L)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTask() {
        createTestTask("test");

        assertThat(controller.detail(1L).getTitle()).isEqualTo("test");

        controller.delete(1L);

        assertThatThrownBy(() -> controller.detail(1L)).isInstanceOf(TaskNotFoundException.class);
    }
}