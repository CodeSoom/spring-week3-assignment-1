package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;

    private Task task1;
    private Task task2;
    private List<Task> tasks;

    final private Long VALID_ID = 1L;
    final private Long INVALID_ID = 100L;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);

        task1 = new Task(1L, "title1");
        task2 = new Task(2L, "title2");
        tasks = Arrays.asList(task1, task2);
    }

    @Test
    void list() {
        given(taskService.getTasks()).willReturn(tasks);

        assertThat(taskController.list()).isEqualTo(tasks);
    }

    @Test
    void detailWithValidId() {
        given(taskService.getTask(VALID_ID)).willReturn(task1);

        assertThat(taskController.detail(VALID_ID)).isEqualTo(task1);
    }

    @Test
    void detailWithInvalidId() {
        given(taskService.getTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));

        assertThatThrownBy(() -> taskController.detail(INVALID_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void create() {
        given(taskService.createTask(task1)).willReturn(task1);

        assertThat(taskController.create(task1)).isEqualTo(task1);
    }

    @Test
    void updateWithValidId() {
        Task updatedTask = new Task(VALID_ID, task2.getTitle());
        given(taskService.updateTask(VALID_ID, task2)).willReturn(updatedTask);

        assertThat(taskController.update(VALID_ID, task2)).isEqualTo(updatedTask);
    }

    @Test
    void updateWithInvalidId() {
        given(taskService.updateTask(INVALID_ID, task2)).willThrow(new TaskNotFoundException(INVALID_ID));

        assertThatThrownBy(() -> taskController.update(INVALID_ID, task2))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void patchWithValidId() {
        Task updatedTask = new Task(VALID_ID, task2.getTitle());
        given(taskService.updateTask(VALID_ID, task2)).willReturn(updatedTask);

        assertThat(taskController.patch(VALID_ID, task2)).isEqualTo(updatedTask);
    }

    @Test
    void patchWithInvalidId() {
        given(taskService.updateTask(INVALID_ID, task2)).willThrow(new TaskNotFoundException(INVALID_ID));

        assertThatThrownBy(() -> taskController.patch(INVALID_ID, task2))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteWithInvalidId() {
        given(taskService.deleteTask(INVALID_ID)).willThrow(new TaskNotFoundException(INVALID_ID));

        assertThatThrownBy(() -> taskController.delete(INVALID_ID))
                .isInstanceOf(TaskNotFoundException.class);
    }

}