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

    @DisplayName("setId로 id를 설정하면, getId로 설정한 값을 리턴받을 수 있다")
    @Test
    void setId() {
        task.setId(GIVEN_ID);

        assertThat(task.getId()).isEqualTo(GIVEN_ID);
    }

    @DisplayName("setTitle로 title를 설정하면, getTitle로 설정한 값을 리턴받을 수 있다")
    @Test
    void setTitle() {
        task.setTitle(GIVEN_TITLE);

        assertThat(task.getTitle()).isEqualTo(GIVEN_TITLE);
    }


}
