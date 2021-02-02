package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    @Test
    void list() {
        TaskController controller = new TaskController();

        assertThat(controller.list()).isEmpty();
    }

    @Test
    void createNewTask() {
        TaskController controller = new TaskController();

        Task task = new Task();
        task.setTitle("test");
        controller.create(new Task());

        assertThat(controller.list()).isNotEmpty();
    }

}
