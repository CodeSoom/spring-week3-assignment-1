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
}
