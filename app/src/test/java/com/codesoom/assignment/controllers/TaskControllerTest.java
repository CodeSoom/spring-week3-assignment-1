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

        task.setTitle("test1");
        controller.create(task);

        assertThat(controller.list()).hasSize(1);
        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
        assertThat(controller.list().get(0).getTitle()).isEqualTo("test1");
    }
}
