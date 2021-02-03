package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    private TaskController controller;

    @BeforeEach
    void setUp(){
       controller = new TaskController();
    }

    @Test
    void list() {
        assertThat(controller.list()).isEmpty();
    }


    @Test
    @DisplayName("check ID and its name")
    void createNewTask(){
        Task task = new Task();
        task.setTitle("Test1");
        controller.create(task);

        assertThat(controller.list()).hasSize(1);
        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
        assertThat(controller.list().get(0).getTitle()).isEqualTo("Test1");

    }

    @Test
    void deleteTask() {


    }
}