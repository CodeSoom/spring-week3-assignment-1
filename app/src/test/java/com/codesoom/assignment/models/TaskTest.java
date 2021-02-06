package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스")
class TaskTest {
    final long TASK_ID = 0L;
    final String TASK_TITLE = "Get Sleep";

    @DisplayName("task 생성")
    @Test
    void create() {
        //given
        Task task;

        //when
        task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);

        //then
        assertThat(task.getId()).isEqualTo(task.getId());
        assertThat(task.getTitle()).isEqualTo(task.getTitle());
    }
}
