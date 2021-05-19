package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTest {
    @Test
    public void testId() {
        Long NEGATIVE_ID = -1L;
        Long ZERO_ID = 0L;
        Long POSITIVE_ID = 1L;

        // 음수인 id를 가지는 Task 인스턴스의 id를 반환받는 경우
        Task task1 = new Task();
        task1.setId(NEGATIVE_ID);
        assertThat(task1.getId()).isEqualTo(NEGATIVE_ID);

        // 0인 id를 가지는 Task 인스턴스의 id를 반환받는 경우
        Task task2 = new Task();
        task2.setId(ZERO_ID);
        assertThat(task2.getId()).isEqualTo(ZERO_ID);

        // 양수인 id를 가지는 Task 인스턴스의 id를 반환받는 경우
        Task task3 = new Task();
        task3.setId(POSITIVE_ID);
        assertThat(task3.getId()).isEqualTo(POSITIVE_ID);
    }

    @Test
    public void testTitle() {
        String EMPTY_TITLE = "";
        String KOREAN_TITLE = "할 일";
        String ENGLISH_TITLE = "Todo";

        Task task1 = new Task();
        task1.setTitle(EMPTY_TITLE);
        assertThat(task1.getTitle()).isEqualTo(EMPTY_TITLE);

        Task task2 = new Task();
        task2.setTitle(KOREAN_TITLE);
        assertThat(task2.getTitle()).isEqualTo(KOREAN_TITLE);

        Task task3 = new Task();
        task3.setTitle(ENGLISH_TITLE);
        assertThat(task3.getTitle()).isEqualTo(ENGLISH_TITLE);
    }
}
