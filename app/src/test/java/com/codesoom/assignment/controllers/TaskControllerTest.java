package com.codesoom.assignment.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.codesoom.assignment.models.Task;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskControllerTest {

    private TaskController controller;

    @BeforeEach
    void setUp() {
        controller = new TaskController();
    }

    @Test
    void list() {
        List<Task> list = controller.list();

        assertThat(list).isEmpty();
    }

    @Test
    void createNewTask() {
        List<Task> list = controller.list();
        assertThat(list).isEmpty();

        controller.create(new Task());

        assertThat(list).isNotEmpty();
        assertThat(list).hasSize(1);
    }
}
