package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskTest")
class TaskTest {
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle("testTitle");
        task.setId(1L);
    }

    @Test
    @DisplayName("Task의 id를 반환한다")
    void getId() {
        // given
        Task task = new Task();
        task.setId(1L);

        // when
        Long getId = task.getId();

        // then
        assertThat(task.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Task의 id를 설정한다")
    void setId() {

    }

    @Test
    @DisplayName("Task의 title을 반환한다")
    void getTitle() {
        // given
        Task task = new Task();
        task.setTitle("Test Title");

        // when
        String getTitle = task.getTitle();

        // then
        assertThat(getTitle).isEqualTo("Test Title");
    }

    @Test
    @DisplayName("Task의 title을 설정한다")
    void setTitle() {

    }
}