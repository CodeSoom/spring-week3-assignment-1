package com.codesoom.assignment.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 테스트")
class TaskTest {
    public static final String TITLE = "TITLE";
    public static final Long UPDATE_ID = 1L;
    public static final String UPDATE_TITLE = "UPDATE_TITLE";

    @Test
    @DisplayName("기본 사용법 (Getter,Setter)")
    public void setter() {
        Task task = new Task(0L, TITLE);
        task.setTitle(UPDATE_TITLE);
        task.setId(UPDATE_ID);

        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
        assertThat(task.getId()).isEqualTo(UPDATE_ID);
    }
}
