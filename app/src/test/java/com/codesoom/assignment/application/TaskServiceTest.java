package com.codesoom.assignment.application;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.IdGeneratorImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskServiceTest {
    private static final String TEST_TITLE = "Test Title";
    private final Task task = new Task(1L, TEST_TITLE);
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        TaskRepository taskRepository = new TaskRepository(new IdGeneratorImpl());
        taskRepository.addTask(task);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void getTaskList() {
        Assert.assertEquals(taskService.getTaskList().size(), 1);
    }

    @Test
    void getTaskById() {
        Assertions.assertEquals(TEST_TITLE, taskService.getTaskById(1L).get().getTitle());
    }

    @Test
    void addTask() {
        taskService.addTask(new Task(2L, "Test title2"));
        Assertions.assertEquals(taskService.getTaskList().size(), 2);
    }

    @Test
    void replaceTask() {
        Task newTask = new Task(1L, "New Title");
        Assertions.assertEquals("New Title", taskService.replaceTask(1L, newTask).get().getTitle());
    }

    @Test
    void updateTask() {
        Task newTask = new Task(1L, "New Title");
        Assertions.assertEquals("New Title", taskService.replaceTask(1L, newTask).get().getTitle());
    }

    @Test
    void deleteTask() {
        Assertions.assertEquals(1L, taskService.deleteTask(1L).get().getId());
    }
}