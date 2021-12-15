package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle("TEST");

        taskController.create(task);
    }


    @Test
    void list () {
        List<Task> tasks = new ArrayList<>();

        assertThat(taskController.list()).hasSize(1);
//        assertThat(taskController.list().get(0).getTitle()).isEqualTo("TEST");

    }

//    @Test
//    void create() {
//        TaskController taskController = new TaskController(taskService);
//
//
//
//        assertThat(taskController.list()).isEmpty();
//    }
}
