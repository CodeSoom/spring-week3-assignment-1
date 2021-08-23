package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.TaskService;
import com.codesoom.assignment.models.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    TaskService taskService = new TaskService();

    @Test
    void list() {
        TaskController controller = new TaskController(taskService);

        Assertions.assertThat(controller.list()).isEmpty();

    }

}