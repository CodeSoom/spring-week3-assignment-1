package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskControllerTest {
    private static final String TASK_TITLE_ONE = "testOne";
    private static final String TASK_TITLE_TWO = "testTwo";
    private static final String UPDATE_TITLE = "otherTest";

    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(TASK_TITLE_ONE);
        controller.create(task);
    }

    @Test
    void list() {
        assertThat(controller.list()).isNotEmpty();
        assertThat(controller.list()).hasSize(1);
    }

    @Test
    void detailWithValidId() {
        assertThat(controller.detail(1L).getTitle()).isEqualTo(TASK_TITLE_ONE);
    }

    @Test
    void detailWithInvalidId() {
        assertThatThrownBy(() -> controller.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void create() {
        int oldSize = controller.list().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE_TWO);
        controller.create(task);

        int newSize = controller.list().size();

        assertThat(newSize - oldSize).isEqualTo(1);
        assertThat(controller.list()).hasSize(2);
        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
        assertThat(controller.list().get(0).getTitle()).isEqualTo(TASK_TITLE_ONE);
        assertThat(controller.list().get(1).getId()).isEqualTo(2L);
        assertThat(controller.list().get(1).getTitle()).isEqualTo(TASK_TITLE_TWO);
    }

    @Test
    void update() {
        Long id = Long.valueOf(controller.list().size());
        Task task = controller.detail(id);

        assertThat(task.getTitle()).isEqualTo(TASK_TITLE_ONE);

        task.setTitle(UPDATE_TITLE);
        controller.update(id, task);
        task = controller.detail(id);

        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @Test
    void patch() {
        Long id = Long.valueOf(controller.list().size());
        Task task = controller.detail(id);

        assertThat(task.getTitle()).isEqualTo(TASK_TITLE_ONE);

        task.setTitle(UPDATE_TITLE);
        controller.patch(id, task);
        task = controller.detail(id);

        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
    }

    @Test
    void delete() {
        int oldSize = controller.list().size();

        controller.delete(Long.valueOf(oldSize));

        int newSize = controller.list().size();

        assertThat(oldSize - newSize).isEqualTo(1);
        assertThat(controller.list()).hasSize(0);
    }
}