package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskControllerTest {
    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task createTask = new Task();
        createTask.setTitle("Test1");
        taskController.create(createTask);
    }

    @Test
    void listTasks() {
        assertThat(taskController.list()).hasSize(1);
    }

    @Test
    void detailTask() {
        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo("Test1");
    }

    @Test
    void createTask() {
        Task createTask = new Task();
        createTask.setTitle("Test2");
        taskController.create(createTask);

        assertThat(taskController.list()).hasSize(2);
        assertThat(taskController.list().get(1).getId()).isEqualTo(2L);
        assertThat(taskController.list().get(1).getTitle()).isEqualTo("Test2");
    }

    @Test
    void updateTask() {
        Task updateTask = new Task();
        updateTask.setTitle("new Test");
        taskController.update(1L, updateTask);

        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo("new Test");
    }

    @Test
    void patchTask() {
        Task updateTask = new Task();
        updateTask.setTitle("new Test");
        taskController.patch(1L, updateTask);

        assertThat(taskController.list().get(0).getId()).isEqualTo(1L);
        assertThat(taskController.list().get(0).getTitle()).isEqualTo("new Test");
    }

    @Test
    void deleteTask() {
        taskController.delete(1L);

        assertThat(taskController.list()).isEmpty();
    }
}