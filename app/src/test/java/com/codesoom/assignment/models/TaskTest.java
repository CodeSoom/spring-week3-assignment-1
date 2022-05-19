package com.codesoom.assignment.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

    @DisplayName("Task Equals & HashCode 재정의 테스트")
    @Test
    void taskEqualsTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 1");

        Task task3 = new Task();
        task3.setId(1L);
        task3.setTitle("Task 3");

        Task newTask = new Task();
        newTask.setId(1L);
        newTask.setTitle("Task 1");

        Task otherTask = new Task();
        otherTask.setId(2L);
        otherTask.setTitle("Task 2");

        assertThat(task.equals(newTask)).isTrue();
        assertThat(task.equals(otherTask)).isFalse();
        assertThat(task.equals("test")).isFalse();
        assertThat(task.equals(task2)).isFalse();
        assertThat(task.equals(task3)).isFalse();
        assertThat(task.hashCode()).isEqualTo(newTask.hashCode());
    }
}
