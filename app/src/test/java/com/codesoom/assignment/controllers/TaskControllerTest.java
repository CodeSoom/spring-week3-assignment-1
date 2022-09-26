package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskControllerTest {
    private TaskService taskService;
    private TaskController taskController;
    Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);
        task = new Task();
        task.setTitle("title");
        task.setId(1L);
    }

    @Test
    void list() {
        assertThat(taskController.list()).isEmpty();
    }


    @Test
    void detailWithValidId() {
        //given
        Task task = new Task();
        task.setTitle("title");
        task.setId(1L);
        taskService.createTask(task);
        //when
        Task detail = taskController.detail(1L);
        //then
        assertThat(detail.getId()).isEqualTo(task.getId());
    }

    @Test
    void detailWithInvalidId() {
        //given
        taskService.createTask(task);
        //when
        //then
        assertThrows(TaskNotFoundException.class, () -> taskController.detail(100L));

    }

    @Test
    void create() {
        Task task = new Task();
        task.setTitle("1");
        taskController.create(task);
        task.setTitle("2");
        taskController.create(task);

        assertThat(taskController.list()).hasSize(2);
    }

    @Test
    void updatedWithValidId() {
        //given
        taskController.create(task);
        //when
        Task updatedTask = taskController.update(1L, task);
        //then
        assertThat(updatedTask.getTitle()).isEqualTo(updatedTask.getTitle());
        assertThat(updatedTask.getId()).isEqualTo(updatedTask.getId());
    }
//    @Test
//    void update() {
//    }
//
//    @Test
//    void patch() {
//    }
//
//    @Test
//    void delete() {
//    }
}
