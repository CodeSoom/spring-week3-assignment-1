package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;
    final private Long ID = 1L;
    final private String TITLE = "title";

    @BeforeEach
    void setUp() {
        task = new Task(ID, TITLE);
    }

    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(ID);
    }

    @Test
    void setId() {
        Long newId = 2L;
        task.setId(newId);

        assertThat(task.getId()).isEqualTo(newId);
    }

    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Test
    void setTitle() {
        String newTitle = "newTitle";
        task.setTitle(newTitle);

        assertThat(task.getTitle()).isEqualTo(newTitle);
    }
}