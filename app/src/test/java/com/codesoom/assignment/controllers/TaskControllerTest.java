package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    TaskService taskService = new TaskService();
    TaskController controller;

    @BeforeEach
    void setUp() {
        controller = new TaskController(taskService);
    }

    @Test
    void list() {
        //given

        //when

        //then
        assertThat(controller.list()).isEmpty();
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("Test");
        controller.create(task);

        //assertThat(controller.list()).isNotEmpty();
        assertThat(controller.list()).hasSize(1);
    }


}
