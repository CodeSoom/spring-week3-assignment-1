package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("Test to get Task list to use \"list\" on TaskController class")
    @Test
    void list() {
        assertThat(controller.list()).isEmpty();

        createTestTask("test");
        createTestTask("test2");

        assertThat(controller.list()).hasSize(2);
    }

    @DisplayName("Test to get specific Task to use \"getDetail\" on TaskController class")
    @Test
    void getDetail() {
        assertThatThrownBy(() -> controller.detail(1L)).isInstanceOf(TaskNotFoundException.class);

        createTestTask("test");
        createTestTask("test2");

        assertThat(controller.detail(1L).getTitle()).isEqualTo("test");
        assertThat(controller.detail(2L).getTitle()).isEqualTo("test2");
    }

    @DisplayName("Test to create Task to use \"createNewTask\" on TaskController class")
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

    @DisplayName("Test to update Task's title to use \"updateTask\" on TaskController class")
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

    @DisplayName("Test to update Task's title to use \"patchTask\" on TaskController class")
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

    @DisplayName("Test to delete Task to use \"deleteTask\" on TaskController class")
    @Test
    void deleteTask() {
        createTestTask("test");

        assertThat(controller.detail(1L).getTitle()).isEqualTo("test");

        controller.delete(1L);

        assertThatThrownBy(() -> controller.detail(1L)).isInstanceOf(TaskNotFoundException.class);
    }
}
