package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스")
public class TaskTest {
    private static final Long GIVEN_ID = 1L;
    private static final String GIVEN_TITLE = "homework";

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @DisplayName("setId은 Task의 id를 변경한다")
    @Test
    void setId() {
        task.setId(GIVEN_ID);

        assertThat(task.getId()).isEqualTo(GIVEN_ID);
    }

    @DisplayName("setTitle은 Task의 title을 변경한다")
    @Test
    void setTitle() {
        task.setTitle(GIVEN_TITLE);

        assertThat(task.getTitle()).isEqualTo(GIVEN_TITLE);
    }


}
