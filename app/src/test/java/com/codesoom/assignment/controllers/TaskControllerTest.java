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

    private static final String TASK_TITLE = "test1";
    private static final String TASK_TITLE2 = "test2";
    private static final String UPDATE_POSTFIX = "!!!";

    @BeforeEach
    void setUp() {
        taskService = new TaskService();

        // 가능 한것
        // 1. Real object
        // 2. Mock object
        // 3. Spy -> Proxy
        controller = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(TASK_TITLE);

        controller.create(task);
    }

    @Test
    void listWithoutTasks() {
        // TODO : service -> returns empty list

        // taskService.getTask
        //Controller -> Sqy -< Real Object

        assertThat(controller.list()).isEmpty();
    }

    @Test
    void listWithSomeTasks() {
        // TODO : service -> returns list that contains one task.

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        controller.create(task);

        assertThat(controller.list()).isNotEmpty();
    }

    @Test
    void createNewTask() {
        int oldSize = controller.list().size();

        Task task = new Task();
        task.setTitle(TASK_TITLE2);
        controller.create(task);

        int newSize = controller.list().size();

        assertThat(newSize - oldSize).isEqualTo(1);
        assertThat(controller.list().get(1).getId()).isEqualTo(2L);
        assertThat(controller.list().get(1).getTitle()).isEqualTo(TASK_TITLE2);
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

    @Test
    void updateWithValidId() {
        Task source = new Task();
        source.setTitle(UPDATE_POSTFIX + TASK_TITLE);
        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(UPDATE_POSTFIX + TASK_TITLE);
    }

    @Test
    void patchWithValidId() {
        Task source = new Task();
        source.setTitle(UPDATE_POSTFIX + TASK_TITLE2);
        taskService.updateTask(1L, source);

        Task task = taskService.getTask(1L);
        assertThat(task.getTitle()).isEqualTo(UPDATE_POSTFIX + TASK_TITLE2);
    }

    @Test
    void delete() {
        controller.delete(1L);

        assertThat(controller.list().size()).isEqualTo(0);
    }
}
