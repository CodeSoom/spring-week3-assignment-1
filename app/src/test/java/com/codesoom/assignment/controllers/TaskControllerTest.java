package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskController 클래스")
class TaskControllerTest {

    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        this.taskService = new TaskService();
        this.taskController = new TaskController(taskService);
    }

    @Test
    @DisplayName("list는 모든 할 일 목록을 반환한다.")
    void list() {
        assertThat(taskService.getTasks()).isEmpty();
    }
}