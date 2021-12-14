package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void setter() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("test1");

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("test1");
    }

    @Test
    void nullPointException() {
        Task task = null;

        assertThatThrownBy(task::getId)
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> task.setId(1L))
                .isInstanceOf(NullPointerException.class);
    }
}