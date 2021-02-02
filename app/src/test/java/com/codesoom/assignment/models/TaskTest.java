package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static final String TASK_TITLE = "test";

    @Test
    void GetTask() {
        Task task = new Task();
        task.setTitle(TASK_TITLE);

        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }
}
