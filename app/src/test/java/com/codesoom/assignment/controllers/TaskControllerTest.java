package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    private TaskService service;
    private TaskController controller;

    @BeforeEach
    void setUp() {
        this.service = new TaskService();
        this.controller = new TaskController(this.service);
    }

    @Test
    void list() {
        // then
        assertThat(controller.list()).isEmpty();
    }

    @Test
    void createNewTask() {
        // given
        Task task = new Task();
        task.setTitle("Test");

        // when
        controller.create(task);

        // then
        assertThat(controller.list()).isNotEmpty();
        assertThat(controller.list()).hasSize(1);
    }

}