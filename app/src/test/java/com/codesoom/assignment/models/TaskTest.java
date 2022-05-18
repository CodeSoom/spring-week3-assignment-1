package com.codesoom.assignment.models;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @DisplayName("Task 인스턴스 생성 테스트")
    @Test
    void generateTaskInstance() {

        assertThat(task).isNotNull();
    }

    @DisplayName("Task 인스턴스 생성 후 아이디 설정 테스트")
    @Test
    void generateTaskInstanceWithId() {

        long taskId = 1L;

        task.setId(taskId);

        assertThat(task.getId()).isEqualTo(taskId);
    }

    @DisplayName("Task 인스턴스 생성 후 제목 설정 테스트")
    @Test
    void generateTaskInstanceWithTitle() {

        String taskTitle = "Hello Task!";

        task.setTitle(taskTitle);

        assertThat(task.getTitle()).isEqualTo(taskTitle);
    }
}
