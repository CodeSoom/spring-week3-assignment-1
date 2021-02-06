package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task 클래스")
public class TaskTest {
    private static final Long GIVEN_ID = 1L;
    private static final Long CHANGE_ID = 1L;
    private static final String GIVEN_TITLE = "homework";

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(GIVEN_ID);
        task.setTitle(GIVEN_TITLE);
    }

    @DisplayName("Task를 생성한다")
    @Test
    void create() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("STUDY");

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("STUDY");
    }

    @DisplayName("setId은 Task의 id를 변경한다")
    @Test
    void setId() {
        task.setId(CHANGE_ID);

        assertThat(task.getId()).isEqualTo(CHANGE_ID);
    }

    @DisplayName("setTitle은 Task의 title을 변경한다")
    @Test
    void setTitle() {
        task.setTitle(GIVEN_TITLE);

        assertThat(task.getTitle()).isEqualTo(GIVEN_TITLE);
    }

    @DisplayName("getId은 Task의 id을 리턴한다")
    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(CHANGE_ID);
    }

    @DisplayName("getTitle은 Task의 title을 리턴한다")
    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(GIVEN_TITLE);
    }

}
