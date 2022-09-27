package com.codesoom.assignment.controllers;

import com.codesoom.assignment.appllication.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskController controller;

    private final String TEST_TITLE = "TEST_TITLE";
    private final String UPDATE_TITLE = "UPDATE_TITLE";

    @BeforeEach
    void setUp() {
        TaskService taskService = new TaskService();
        controller = new TaskController(taskService);

        Task task = new Task();
        task.updateTitle(TEST_TITLE);
        controller.create(task);
    }

    @Test
    void list() {
        assertThat(controller.list()).hasSize(1);
    }

    @Test
    void detailTask() {
        assertThat(controller.detail(1L).getTitle()).isEqualTo(TEST_TITLE);
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.updateTitle("Test");
        controller.create(task);

        assertThat(controller.list()).hasSize(2);
        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void updateTask() {
        Task source = new Task();
        source.updateTitle(UPDATE_TITLE);

        assertThat(controller.patch(1L, source).getTitle()).isEqualTo(UPDATE_TITLE);
        assertThat(controller.put(1L, source).getTitle()).isEqualTo(UPDATE_TITLE);

    }

    @Test
    void deleteTask() {
        controller.delete(1L);
        assertThat(controller.list()).hasSize(0);
    }





}