package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskController 클래스")
class TaskControllerTest {
    private static final String Task_Title_One = "test_One";

    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        taskController = new TaskController(taskService);

        Task task = new Task();
        task.setTitle(Task_Title_One);
        taskController.create(task);
    }

    @Test
    @DisplayName("기본적으로 생성된 Task 가 하나만 존재한다면, list 메서드로 조회시 Task 의 갯수는 1개이다.")
    void list() {
        final List<Task> list = taskController.list();

        assertThat(list).hasSize(1);
    }
}
