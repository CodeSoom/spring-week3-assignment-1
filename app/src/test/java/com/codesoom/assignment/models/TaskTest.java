package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @DisplayName("setter 메소드로 수정하면 getter 메소드로 조회할 수 있다")
    @Test
    void setter_getter() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("test1");

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("test1");
    }
}