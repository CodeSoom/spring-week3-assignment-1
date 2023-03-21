package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;

    private static final String TITLE = "Test";
    private static final String UPDATE_TITLE = "TestUpdate";
    private static final Long ID = 1L;
    private static final Long UPDATE_ID = 100L;

    @BeforeEach
    void setUp(){
        this.task = new Task();
        task.setId(ID);
        task.setTitle(TITLE);
    }
    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(ID);
    }

    @Test
    void setId() {
        task.setId(UPDATE_ID);
        assertThat(task.getId()).isEqualTo(UPDATE_ID);
    }

    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Test
    void setTitle() {
        task.setTitle(UPDATE_TITLE);
        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
    }
}