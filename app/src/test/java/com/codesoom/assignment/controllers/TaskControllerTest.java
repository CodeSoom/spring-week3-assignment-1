package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TaskControllerTest {
    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        controller = new TaskController(taskService);

        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTitle("Task1");
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(1L)).willReturn(task);

        given(taskService.getTask((100L)))
                .willThrow(new TaskNotFoundException(100L));

        given(taskService.updateTask(eq(100L), any(Task.class)))
                .willThrow(new TaskNotFoundException(100L));

        given(taskService.deleteTask(100L))
                .willThrow(new TaskNotFoundException(100L));
    }

    @Test
    void listWithoutTasks() {
        given(taskService.getTasks()).willReturn(new ArrayList<>());

        assertThat(controller.list()).isEmpty();

        verify(taskService).getTasks();
    }

    @Test
    void listWithSomeTasks() {
        assertThat(controller.list()).isNotEmpty();

        verify(taskService).getTasks();
    }

    @Test
    void detailWithExistingId() {
        assertThat(controller.detail(1L)).isNotNull();

        verify(taskService).getTask(1L);
    }

    @Test
    void detailWithNotExistingId() {
        assertThatThrownBy(() -> controller.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskService).getTask(100L);
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("Task2");

        controller.create(task);

        verify(taskService).createTask(task);
    }

    @Test
    void updateExistingTask() {
        Task task = new Task();
        task.setTitle("Updated Task1");

        controller.update(1L, task);

        verify(taskService).updateTask(1L, task);
    }

    @Test
    void updateNotExistingTask() {
        Task task = new Task();
        task.setTitle("Updated Task1");

        assertThatThrownBy(() -> controller.update(100L, task))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void patchExistingTask() {
        Task task = new Task();
        task.setTitle("Updated Task1");

        controller.patch(1L, task);

        verify(taskService).updateTask(1L, task);
    }

    @Test
    void patchNotExistingTask() {
        Task task = new Task();
        task.setTitle("Updated Task1");

        assertThatThrownBy(() -> controller.patch(100L, task))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteExistingTask() {
        controller.delete(1L);

        verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteNotExistingTask() {
        assertThatThrownBy(() -> controller.delete(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
