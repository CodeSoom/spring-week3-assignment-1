package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskErrorAdviceTest {
    private TaskErrorAdvice taskErrorAdvice;

    @BeforeEach
    void setUp() {
        taskErrorAdvice = new TaskErrorAdvice();
    }

    @Test
    void handleNotFound() {
        assertThat(taskErrorAdvice.handleNotFound().getMessage()).isEqualTo("Task not found");
    }
}