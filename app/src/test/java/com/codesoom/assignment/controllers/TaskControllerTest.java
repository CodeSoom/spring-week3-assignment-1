package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskControllerTest {
    @Test
    void list() {
        TaskController controller = new TaskController();

        assertThat(controller.list()).isEmpty();
    }
}
