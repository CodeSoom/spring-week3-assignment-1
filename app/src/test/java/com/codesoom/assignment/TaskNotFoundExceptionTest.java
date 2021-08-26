package com.codesoom.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TaskNotFoundExceptionTest {

    private static final long id = 1L;

    @Test
    void exception() {
        assertThat(new TaskNotFoundException(id))
            .hasMessage("Task not found: " + id);
    }
}
