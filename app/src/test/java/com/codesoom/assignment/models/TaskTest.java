package com.codesoom.assignment.models;

import com.codesoom.assignment.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void getIdWithValid() {
        task.setId(1L);
        assertThat(task.getId()).isEqualTo(1L);
    }

    @Test
    void getIdWithInvalid() {
        assertThat(task.getId()).isNull();
    }


    @Test
    void setId() {
        task.setId(2L);
        assertThat(task.getId()).isEqualTo(2L);
    }

    @Test
    void getTitleWithValidId() {
        task.setTitle("test");
        assertThat(task.getTitle()).isEqualTo("test");
    }

    @Test
    void getTitleWithInvalid() {
        assertThat(task.getTitle()).isNull();
    }

    @Test
    void setTitle() {
        task.setTitle("test");
        assertThat(task.getTitle()).isEqualTo("test");
    }
}