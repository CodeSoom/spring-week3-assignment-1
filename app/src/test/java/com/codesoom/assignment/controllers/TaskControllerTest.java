package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {
    @Test
    void list() {
        TaskController controller = new TaskController();
        assertThat(controller.list()).isEmpty();
    }
}