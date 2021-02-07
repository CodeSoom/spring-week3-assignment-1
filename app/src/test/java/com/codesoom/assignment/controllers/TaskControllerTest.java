package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;

class TaskControllerTest {

    TaskService taskService;
    TaskController controller;
    List<Task> tasks;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        controller = new TaskController(taskService);

        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTitle("Test1");
        tasks.add(task);

        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(1L)).willReturn(task);
        given(taskService.getTask(100L))
            .willThrow(new TaskNotFoundException(100L));
        given(taskService.updateTask(eq(100L), any(Task.class)))
            .willThrow(new TaskNotFoundException(100L));
        given(taskService.deleteTask((100L)))
            .willThrow(new TaskNotFoundException(100L));
    }

    @Test
    void listWithoutTasks() {
        given(taskService.getTasks()).willReturn(new ArrayList<>());

        assertThat(controller.list()).isEmpty();

    }

    @Test
    void listWithSomeTasks() {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTitle("test1");
        tasks.add(task);
        given(taskService.getTasks()).willReturn(tasks);

        assertThat(controller.list()).isNotEmpty();

        verify(taskService).getTasks();
    }

    @Test
    void detail() {
        Task result = controller.detail(1L);
        assertThat(result).isNotNull();
    }

    @Test
    void detailWithNotExistedID() {
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
    void updateExistedTask() {
        Task task = new Task();
        task.setTitle("renamed task");

        controller.update(1L, task);

        verify(taskService).updateTask(1L, task);
    }

    @Test
    void updateNotExistedTask() {
        Task task = new Task();
        task.setTitle("renamed task");

        assertThatThrownBy(() -> controller.update(100L, task))
            .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void deleteExistedTask() {
        controller.delete(1L);

        verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteNotExistedTask() {
        assertThatThrownBy(() -> controller.delete(100L))
            .isInstanceOf(TaskNotFoundException.class);
    }

}
