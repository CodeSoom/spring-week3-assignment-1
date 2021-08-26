package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskRepository;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskIdGeneratorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class TaskControllerTest {
    private static final String TEST_TITLE = "Test Title";
    private final Task task = new Task(1L, TEST_TITLE);
    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        TaskRepository taskRepository = new TaskRepository(new TaskIdGeneratorImpl());
        taskRepository.addTask(task);
        TaskService taskService = new TaskService(taskRepository);
        taskController = new TaskController(taskService);
    }

    @Test
    void getTaskList() {
        Assertions.assertEquals(1, taskController.getTaskList().size());
    }

    @Test
    void getTaskById() {
        Assertions.assertEquals(TEST_TITLE, taskController.getTaskById(1L).getTitle());
    }

    @Test
    void addTask() {
        Assertions.assertEquals(2, taskController.addTask(new Task(2L, "Test Title2")).getId());
    }

    @Test
    void replaceTask() {
        Assertions.assertEquals("Test title1", taskController.replaceTask(1L, new Task(1L, "Test title1")));
    }

    @Test
    void updateTask() {
        Assertions.assertEquals("Test title1", taskController.replaceTask(1L, new Task(1L, "Test title1")));
    }

    @Test
    void deleteTask() {
        taskController.deleteTask(1L);
        Assertions.assertEquals(0, taskController.getTaskList().size());
    }
}