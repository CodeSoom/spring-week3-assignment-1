package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import org.junit.jupiter.api.BeforeEach;

class TaskControllerTest {
    private TaskController controller;
    private TaskService taskService;
    private static final String TASK_TITLE = "test";

    @BeforeEach
    void SetUp() {
        taskService = new TaskService();
        controller = new TaskController(taskService); // 주입
    }
}