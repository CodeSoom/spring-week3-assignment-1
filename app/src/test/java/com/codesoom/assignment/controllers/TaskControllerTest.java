package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TaskControllerTest {
    private TaskController controller;
    private TaskService taskService;

    private static final String TASK_TITLE = "Test";
    private static final String UPDATE_PREFIX = "Update";

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);

        Task task = new Task();
        task.setTitle(TASK_TITLE);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(1L)).willReturn(task);
        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
        given(taskService.updateTask(eq(100L), any(Task.class))).willThrow(new TaskNotFoundException(100L));
        given(taskService.deleteTask(100L)).willThrow(new TaskNotFoundException(100L));

        controller = new TaskController(taskService);
    }

    @Test
    void listWithSomeTasks() {
        assertThat(controller.list()).isNotEmpty();

        verify(taskService).getTasks();
    }

    @Test
    void listWithoutTasks() {
        given(taskService.getTasks()).willReturn(new ArrayList<>());

        assertThat(controller.list()).isEmpty();

        verify(taskService).getTasks();
    }

    @Test
    void detailWithExistId() {
        Task task = controller.detail(1L);

        assertThat(task).isNotNull();
    }

    @Test
    void detailWithNotExistId() {
        assertThatThrownBy(() -> controller.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("Test2");

        controller.create(task);

        verify(taskService).createTask(task);
    }

    @Test
    void deleteTaskWithExistId() {
       controller.delete(1L);

       verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteTaskWithNotExistId() {
        assertThatThrownBy(() -> controller.delete(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void updateTaskWithExistId() {
        Task task = new Task();
        task.setTitle(UPDATE_PREFIX + TASK_TITLE);

        controller.update(1L, task);

        verify(taskService).updateTask(1L, task);
    }

    @Test
    void updateTaskWithNotExistId() {
        Task task = new Task();
        task.setTitle(UPDATE_PREFIX + TASK_TITLE);

        assertThatThrownBy(() -> controller.update(100L, task))
                .isInstanceOf(TaskNotFoundException.class);
    }
}