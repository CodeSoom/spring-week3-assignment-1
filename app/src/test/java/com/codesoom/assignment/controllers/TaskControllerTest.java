package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    private final Long ID = 1L;
    private final String TITLE = "SAMPLE TITLE";
    private final String UPDATED = "[UPDATED]";

    private TaskController controller;
    private TaskService taskService;
    private Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        task = new Task();
        task.setId(ID);
        task.setTitle(TITLE);

        taskService.createTask(task);
    }

    @Test
    void tasks() {
        List<Task> tasks = controller.list();
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(1);
    }

    @Test
    void task() {
        Task task = controller.detail(ID);

        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo(ID);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("Test");
        controller.create(task);

        task.setTitle("Test2");
        controller.create(task);

        assertThat(controller.list()).hasSize(3);

        assertThat(controller.list().get(0).getId()).isEqualTo(ID);
        assertThat(controller.list().get(0).getTitle()).isEqualTo(TITLE);

        assertThat(controller.list().get(1).getId()).isEqualTo(2L);
        assertThat(controller.list().get(1).getTitle()).isEqualTo("Test");

        assertThat(controller.list().get(2).getId()).isEqualTo(3L);
        assertThat(controller.list().get(2).getTitle()).isEqualTo("Test2");
    }

    @Test
    void updateTask() {
        Task task = controller.detail(ID);
        task.setTitle(TITLE + UPDATED);

        Task updatedTask = controller.update(ID, task);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getId()).isEqualTo(ID);
        assertThat(updatedTask.getTitle()).isEqualTo(TITLE + UPDATED);
    }

    @Test
    void patchTask() {
        Task newTask = new Task();
        newTask.setTitle(TITLE);

        newTask = controller.create(newTask);
        Long newTaskId = newTask.getId();
        newTask.setTitle(TITLE + UPDATED);

        Task patchedTask = controller.update(newTaskId, newTask);

        assertThat(patchedTask).isNotNull();
        assertThat(patchedTask.getId()).isEqualTo(newTaskId);
        assertThat(patchedTask.getTitle()).isEqualTo(TITLE + UPDATED);
    }

    @Test
    void deleteTask() {
        List<Task> tasks = controller.list();
        assertThat(tasks).hasSize(1);

        controller.delete(ID);

        tasks = controller.list();
        assertThat(tasks).hasSize(0);
    }
}