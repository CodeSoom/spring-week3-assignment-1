package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private static final Long FIRST_ID = 1L;
    private static final String FIRST_TITLE = "titleOne";
    private static final Long SECOND_ID = 2L;
    private static final String SECOND_TITLE = "titleTwo";

    private Task task;
    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(FIRST_ID);
        task.setTitle(FIRST_TITLE);
    }

    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(FIRST_ID);
    }

    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(FIRST_TITLE);
    }

    @Test
    void setId() {
        assertThat(task.getId()).isEqualTo(FIRST_ID);

        task.setId(SECOND_ID);

        assertThat(task.getId()).isNotEqualTo(FIRST_ID);
        assertThat(task.getId()).isEqualTo(SECOND_ID);
    }

    @Test
    void setTitle() {
        assertThat(task.getTitle()).isEqualTo(FIRST_TITLE);

        task.setTitle(SECOND_TITLE);

        assertThat(task.getTitle()).isNotEqualTo(FIRST_TITLE);
        assertThat(task.getTitle()).isEqualTo(SECOND_TITLE);
    }
}
