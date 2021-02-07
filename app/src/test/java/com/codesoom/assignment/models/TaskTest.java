package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    Task task;
    final Long ID = 1L;
    final Long NEW_ID = 2L;
    final String TITLE = "old title";
    final String NEW_TITLE = "new title";

    @BeforeEach
    void setup() {
        task = new Task();
        task.setId(ID);
        task.setTitle("old title");
    }

    @Test
    @DisplayName("Task 모델 사용 확인")
    void create(){
        Task task = new Task();
        task.setId(3L);
        task.setTitle("사용법");

        assertThat(task.getId()).isEqualTo(3L);
        assertThat(task.getTitle()).isEqualTo("사용법");
    }

    @Test
    @DisplayName("getId() 테스트")
    void getIdTest() {
        assertThat(task.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("setId() 테스트")
    void setIdTest() {
        task.setId(NEW_ID);
        assertThat(task.getId()).isEqualTo(NEW_ID);
    }

    @Test
    @DisplayName("getTitle() 테스트")
    void getTitleTest() {
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }
    @Test
    @DisplayName("setTitle() 테스트")
    void setTitleTest() {
        task.setTitle(NEW_TITLE);
        assertThat(task.getTitle()).isEqualTo(NEW_TITLE);
    }


}
