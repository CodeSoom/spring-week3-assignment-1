package com.codesoom.assignment.models;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.TaskNotFoundException;
import org.junit.jupiter.api.Test;

public class TaskNotFoundExceptionTest {

    @Test
    void exception() {
        assertThatThrownBy(() -> new TaskNotFoundException(1L)).hasMessage("Task not found: " + 1L);
    }
}
