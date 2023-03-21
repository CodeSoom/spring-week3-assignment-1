package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TaskTest {
    public static final String TITLE = "TITLE";
    public static final Long UPDATE_ID = 1L;
    public static final String UPDATE_TITLE = "UPDATE_TITLE";
    Task task;


    @BeforeEach
    public void init() {
        task = new Task(0L, TITLE);
    }

    @Test
    @DisplayName("getter 메소드 테스트")
    public void getter() {
        assertThat(task.getTitle()).isEqualTo(TITLE);
        assertThat(task.getId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("setter 메소드 테스트")
    public void setter() {
        task.setTitle(UPDATE_TITLE);
        task.setId(UPDATE_ID);

        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
        assertThat(task.getId()).isEqualTo(UPDATE_ID);
    }
}
