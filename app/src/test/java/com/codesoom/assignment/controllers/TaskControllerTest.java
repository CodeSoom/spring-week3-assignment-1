package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {
    private TaskController taskController;
    @BeforeEach
    void initTaskController() {
        TaskService taskService = new TaskService();
        taskController = new TaskController(taskService);
    }

    @Test
    void list() {
        assertTrue(taskController.list().isEmpty());
    }

    @Test
    void detail() {
        Task task = new Task();
        task.setTitle("Play!");
        Task createdTask = taskController.create(task);

        Task foundTask = taskController.detail(createdTask.getId());

        assertEquals("Play!", foundTask.getTitle());
    }

    @Test
    void create() {
        Task task = new Task();
        task.setTitle("Play!");

        Task createdTask = taskController.create(task);

        assertEquals("Play!", createdTask.getTitle());
    }

    @Test
    void update() {
        Task task = new Task();
        task.setTitle("Play!");
        Task createdTask = taskController.create(task);

        task.setTitle("Listen");
        Task updatedTask = taskController.update(createdTask.getId(), task);

        assertEquals("Listen", updatedTask.getTitle());
    }

    @Test
    void patch() {
        Task task = new Task();
        task.setTitle("Play!");
        Task createdTask = taskController.create(task);

        task.setTitle("Listen");
        Task updatedTask = taskController.patch(createdTask.getId(), task);

        assertEquals("Listen", updatedTask.getTitle());
    }

    @Test
    void delete() {
        Task task = new Task();
        task.setTitle("Play!");
        Task createdTask = taskController.create(task);

        taskController.delete(createdTask.getId());

        assertTrue( taskController.list().isEmpty());
    }
}