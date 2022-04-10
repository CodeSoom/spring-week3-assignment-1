package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskService service;
    private TaskController controller;

    @BeforeEach
    void setUp() {
        service = new TaskService();
        controller = new TaskController(service);
    }

    @Test
    void list() {
        assertThat(controller.list()).isEmpty();
    }
}
