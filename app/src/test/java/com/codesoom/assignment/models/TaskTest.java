package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 모델")
class TaskTest extends TaskModel {
    static final Long MODIFIED_TASK_ID = 2L;
    static final String MODIFIED_TASK_TITLE = "test2";

    @Test
    @DisplayName("getId 메서드는 id를 리턴한다")
    void getId() {
        Task task = subject();
        assertThat(task.getId()).isEqualTo(TASK_ID);
    }

    @Test
    @DisplayName("getTitle 메서드는 title를 리턴한다")
    void getTitle() {
        Task task = subject();
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("setId 메서드는 주어진 id를 저장한다")
    void setId() {
        Task task = subject();
        task.setId(MODIFIED_TASK_ID);
        assertThat(task.getId()).isEqualTo(MODIFIED_TASK_ID);
    }

    @Test
    @DisplayName("setTitle 메서드는 주어진 title를 저장한다")
    void setTitle() {
        Task task = subject();
        task.setTitle(MODIFIED_TASK_TITLE);
        assertThat(task.getTitle()).isEqualTo(MODIFIED_TASK_TITLE);
    }
}
