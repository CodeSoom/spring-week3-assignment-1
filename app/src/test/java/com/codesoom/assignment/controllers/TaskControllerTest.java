package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskController controller;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService);

        Task task = new Task();
        task.setTitle("Test");
        controller.create(task);
    }

//    @Test
//    void list() {
//        assertThat(controller.list()).isEmpty();
//    }

    @Test
    void createNewTask() {
        Task task = new Task();

        int oldSize = controller.list().size();

        task.setTitle("Test");
        controller.create(task);

        int newSize = controller.list().size();

        assertThat(newSize - oldSize).isEqualTo(1);
    }

    @Test
    void deleteTask() {
        int oldSize = controller.list().size();

        controller.delete(1L);

        int newSize = controller.list().size();

        assertThat(oldSize - newSize).isEqualTo(1);
    }
}