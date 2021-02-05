package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Test
    @DisplayName("생성된 할 일의 Id와 타이틀을 확인한다")
    void model() {

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test");

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("Test");
    }
}
