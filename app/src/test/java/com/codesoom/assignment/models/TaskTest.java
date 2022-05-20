package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskTest 메서드는")
class TaskTest {
    private static final Long TASK_ID_ONE = 1L;
    private static final Long TASK_ID_TWO = 2L;
    private static final String TASK_TITLE_ONE = "test_One";
    private static final String TASK_TITLE_TWO = "test_Two";

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(TASK_ID_ONE);
        task.setTitle(TASK_TITLE_ONE);
    }

    @Test
    @DisplayName("id 를 가져올 수 있다.")
    void getId() {
        assertThat(task.getId()).isEqualTo(TASK_ID_ONE);
    }

    @Test
    @DisplayName("id 를 저장할 수 있다.")
    void setId() {
        task.setId(TASK_ID_TWO);
        assertThat(task.getId()).isEqualTo(TASK_ID_TWO);
    }

    @Test
    @DisplayName("title 을 가져올 수 있다.")
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE_ONE);
    }

    @Test
    @DisplayName("title 을 저장할 수 있다.")
    void setTitle() {
        task.setTitle(TASK_TITLE_TWO);
        assertThat(task.getTitle()).isEqualTo(TASK_TITLE_TWO);
    }

}
