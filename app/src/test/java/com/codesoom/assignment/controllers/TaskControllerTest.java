package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TaskControllerTest {
    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        controller = new TaskController(taskService);
    }

    @Test
    void listWithoutTasks() {
        assertThat(controller.list()).isEmpty();

        verify(taskService).getTasks();
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("test1");

        controller.create(task);

        verify(taskService).createTask(task);
    }
}
