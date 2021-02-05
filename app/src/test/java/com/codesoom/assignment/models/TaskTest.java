package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Test
    void create() {
        Long id = 10L;
        String title = "test";

        Task task = new Task();
        task.setId(id);
        task.setTitle(title);

        assertThat(task.getId()).isEqualTo(id);
        assertThat(task.getTitle()).isEqualTo(title);
    }

}
