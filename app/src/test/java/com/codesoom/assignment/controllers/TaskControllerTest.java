package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {

    TaskController taskController;
    TaskService taskService;
    Task task;

    @BeforeEach
    void init(){
        taskService = new TaskService();
        taskController = new TaskController(taskService);
        task = new Task();
        task.setId(1L);
        task.setTitle("책읽기");
    }

    @DisplayName("비어있는 Task 목록")
    @Test
    void testListWithoutContent(){
        assertThat(taskController.list()).isEmpty();
    }

    @DisplayName("존재하는 Task 목록")
    @Test
    void testListWithContent(){
        taskController.create(task);
        assertThat(taskController.list()).isNotEmpty();
    }

    @DisplayName("존재하는 Task")
    @Test
    void testDetailValid(){
        taskController.create(task);
        assertThat(taskController.detail(1L)).isEqualTo(task);
        assertThat(taskController.detail(1L)).isSameAs(task);
    }

}
