package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTest {
    @Test
    void testId() {
        Long negativeId = -1L;
        Long zeroId = 0L;
        Long positiveId = 1L;

        // 음수인 id를 가지는 Task 인스턴스의 id를 반환받는 경우
        Task task1 = new Task();
        task1.setId(negativeId);
        assertThat(task1.getId()).isEqualTo(negativeId);

        // 0인 id를 가지는 Task 인스턴스의 id를 반환받는 경우
        Task task2 = new Task();
        task2.setId(zeroId);
        assertThat(task2.getId()).isEqualTo(zeroId);

        // 양수인 id를 가지는 Task 인스턴스의 id를 반환받는 경우
        Task task3 = new Task();
        task3.setId(positiveId);
        assertThat(task3.getId()).isEqualTo(positiveId);
    }

    @Test
    void testTitle() {
        Task task = new Task();
        task.setTitle("title");
        assertThat(task.getTitle()).isEqualTo(task.getTitle());
    }
}
