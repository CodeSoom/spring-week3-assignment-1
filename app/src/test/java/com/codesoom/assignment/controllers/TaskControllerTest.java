package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {

    private static final String TASK_TITLE = "블로그에 글쓰기";
    private static final String UPDATE_POSTFIX = "!";
    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp(){
        // subject
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        // fixtures
        Task task = new Task();
        task.setTitle(TASK_TITLE);
        taskController.create(task);
    }

    @Test
    void list() {
        assertThat(taskController.list().size()).isEqualTo(1);
    }

    @Test
    void detail() {
        Task task = taskController.detail(1L);

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void create() {
        String title = "청소";
        Task task = new Task();
        task.setTitle(title);
        taskController.create(task);

        assertThat(taskController.list()).hasSize(2);
        assertThat(taskController.list().get(1).getId()).isEqualTo(2L);
        assertThat(taskController.list().get(1).getTitle()).isEqualTo(title);
    }

    @Test
    void update() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);
        taskController.update(1L, source);

        Task task = taskController.detail(1L);

        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    void patch() {
        Task source = new Task();
        source.setTitle(TASK_TITLE + UPDATE_POSTFIX);
        taskController.patch(1L, source);

        Task task = taskController.detail(1L);

        assertThat(task.getTitle()).isEqualTo(TASK_TITLE + UPDATE_POSTFIX);
    }

    @Test
    void delete() {
        taskController.delete(1L);

        assertThat(taskController.list()).hasSize(0);
    }
}