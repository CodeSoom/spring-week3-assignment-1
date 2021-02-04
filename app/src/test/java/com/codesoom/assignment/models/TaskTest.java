package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskTest {

    @Test
    @DisplayName("Task의 id, title에 대해 getter, setter 검사합니다")
    public void createTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("task");

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("task");
    }

}