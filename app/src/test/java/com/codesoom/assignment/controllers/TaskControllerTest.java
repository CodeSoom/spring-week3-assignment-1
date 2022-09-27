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
    private TaskController taskController;
    private Task testTask;
    private final Long INVALID_TASK_ID = 0L;

    @BeforeEach
    void setUp() {
        taskController = new TaskController(new TaskService());
        testTask = taskController.create(new Task(1L, "test1"));
    }

    @Test
    void list() {
        // given
        taskController.create(new Task(2L, "test2"));

        // when
        List<Task> tasks = taskController.list();

        // then
        assertThat(tasks.size()).isEqualTo(2);
    }

    @Test
    void detailWithValidId() {
        // given
        Long id = testTask.getId();

        // when
        Task task = taskController.detail(id);

        // then
        assertThat(task.getId()).isEqualTo(id);
        assertThat(task.getTitle()).isEqualTo(testTask.getTitle());
    }

    @Test
    void detailWithInvalidId() {
        assertThatThrownBy(
                () -> taskController.detail(INVALID_TASK_ID)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }
    @Test
    void create() {
        // given
        Task newTask = new Task(null, "task2");

        // when
        Task createTask = taskController.create(newTask);

        // then
        assertThat(createTask.getId()).isEqualTo(2L);
        assertThat(createTask.getTitle()).isEqualTo(newTask.getTitle());
    }

    @Test
    void updateWithValidId() {
        // given
        Long id = testTask.getId();
        Task task = new Task(null, "update Task");

        // when
        Task updateTask = taskController.update(id, task);

        // then
        assertThat(updateTask.getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    void updateWithInvalidId() {
        Task task = new Task(null, "update Task");
        assertThatThrownBy(
                () -> taskController.update(INVALID_TASK_ID, task)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTaskWithValidId() {
        // given
        Long id = testTask.getId();

        // when
        taskController.delete(id);

        // then
        assertThatThrownBy(
                () -> taskController.detail(id)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteTaskWithInvalidId() {
        assertThatThrownBy(
                () -> taskController.delete(INVALID_TASK_ID)
        ).isExactlyInstanceOf(TaskNotFoundException.class);
    }
}