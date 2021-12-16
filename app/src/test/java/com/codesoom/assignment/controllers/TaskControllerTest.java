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

        task.setTitle("Test1");
        controller.create(task);

        task.setTitle("Test2");
        controller.create(task);

        assertThat(controller.list()).isNotEmpty();

        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
        assertThat(controller.list().get(0).getTitle()).isEqualTo("Test1");

        assertThat(controller.list().get(1).getId()).isEqualTo(2L);
        assertThat(controller.list().get(1).getTitle()).isEqualTo("Test2");

        assertThat(controller.list()).hasSize(2);
    }
}