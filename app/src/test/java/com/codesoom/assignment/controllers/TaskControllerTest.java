package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class TaskControllerTest {
    private TaskService taskService;
    private TaskController taskController;
    private Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        task = new Task(1L, "title");
    }


    @Test
    void list() {
        assertThat(taskController.list()).isEmpty();
    }


    @Test
    void detailWithValidId() {
        //given
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
        Throwable throwable = catchThrowable(() -> taskController.detail(1L));
        //then
        assertThat(throwable).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("add two tasks and checks the size")
    void createTwoTasks() {
        Task task1 = new Task(1L, "title1");
        taskController.create(task1);
        Task task2 = new Task(2L, "title2");
        taskController.create(task2);

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

    @Test
    void updatedWithInvalidId() {
        //given
        taskController.create(task);
        //when
        Throwable throwable = catchThrowable(() -> taskController.update(2L, task));

        //then
        assertThat(throwable).isInstanceOf(TaskNotFoundException.class);
    }


    @Test
    void deleteWithValidId() {
        //given
        Task createdTask = taskController.create(task);
        //when
        Task deletedTask = taskController.delete(1L);
        //then
        assertThat(createdTask.getTitle()).isEqualTo(deletedTask.getTitle());
        assertThat(createdTask.getId()).isEqualTo(deletedTask.getId());
    }
}
