package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    private static final String TASK_TITLE = "To complete CodeSoom assignment";

    private Task task;

    @BeforeEach
    void init() {
        task = new Task();
    }

    @Test
    @DisplayName("task 번호 세팅하고 조회하기")
    void taskIdTest() {
        task.setId(1L);

        assertThat(task.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("task 제목 세팅하고 조회하기")
    void taskTitleTest() {
        task.setTitle(TASK_TITLE);

        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }
}
