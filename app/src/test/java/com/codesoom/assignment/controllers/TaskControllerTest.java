package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {

    private TaskController controller;
    private TaskService taskService;

    private final static String NEW_TITLE = "new task";
    private final static String TITLE_POSTFIX = " spring";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        Task source = new Task();
        source.setTitle(NEW_TITLE);
        taskService.createTask(source);
    }

    @Test
    void list() {
        List<Task> tasks = controller.list();

        assertThat(tasks).hasSize(1);
    }

    @Test
    void detail_ok() {
        Task task = controller.detail(1L);

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(NEW_TITLE);
    }

    @Test
    void detail_fail() {
        assertThatThrownBy(() -> controller.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void create() {
        Task source = new Task();
        source.setTitle(NEW_TITLE + TITLE_POSTFIX);
        Task task = controller.create(source);

        assertThat(task.getId()).isEqualTo(2L);
        assertThat(task.getTitle()).isEqualTo(NEW_TITLE + TITLE_POSTFIX);
    }

    @Test
    void update() {
        Task source = controller.detail(1L);
        source.setTitle(NEW_TITLE + TITLE_POSTFIX);
        Task update = controller.update(1L, source);

        assertThat(update.getId()).isEqualTo(1L);
        assertThat(update.getTitle()).isEqualTo(NEW_TITLE + TITLE_POSTFIX);
    }

    @Test
    void delete() {
        controller.delete(1L);
        List<Task> tasks = controller.list();

        assertThat(tasks).hasSize(0);
    }
}
