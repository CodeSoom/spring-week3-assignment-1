package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskErrorAdviceTest {

    @Test
    void handleNotFound() {
        TaskErrorAdvice taskErrorAdvice = new TaskErrorAdvice();

        assertThat(taskErrorAdvice.handleNotFound().getMessage()).isEqualTo("Task not found");
    }
}