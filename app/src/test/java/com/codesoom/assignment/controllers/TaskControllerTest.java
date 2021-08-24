package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskControllerTest {

    private static final String TASK_TITLE = "task";
    private static final String NEW_TASK_TITLE = "new";

    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        controller.create(new Task(1L, TASK_TITLE));
    }

    @Test
    void getTasks() {
        List<Task> list = controller.list();

        assertThat(list).hasSize(1);
    }

    @Test
    void createNewTask() {
        controller.create(new Task());

        List<Task> tasks = controller.list();

        assertThat(tasks).hasSize(2);
    }

    @Test
    void getTaskWithValidId() {
        Task task = controller.detail(1L);

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    void getTaskWithInvalidId() {
        assertThatThrownBy(() -> controller.delete(2L))
            .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void updateTaskWithInvalidId() {
        assertThatThrownBy(() -> {
            controller.update(2L, new Task());
        }).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void updateTaskWithValidId() {
        Task newTask = new Task();
        newTask.setTitle(NEW_TASK_TITLE);

        Task updatedTask = controller.update(1L, newTask);

        assertThat(updatedTask.getTitle()).isEqualTo(newTask.getTitle());
    }

    @Test
    void deleteTaskWithInvalidId() {
        assertThatThrownBy(() -> {
            controller.delete(2L);
        }).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTask() {
        controller.delete(1L);

        int size = controller.list().size();

        assertThat(size).isZero();
    }
}
