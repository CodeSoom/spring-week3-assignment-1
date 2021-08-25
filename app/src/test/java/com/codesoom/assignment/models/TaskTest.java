package com.codesoom.assignment.models;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task(1L, "TITLE");
    }

    @Test
    void getter() {
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("TITLE");
    }

    @Test
    void setter() {
        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("TITLE");

        long expectId = 2L;
        String expectTitle = "NEW";

        task.setId(expectId);
        task.setTitle(expectTitle);

        assertThat(task.getId()).isEqualTo(expectId);
        assertThat(task.getTitle()).isEqualTo(expectTitle);
    }

    @Test
    void stringify() {
        assertThat(task.stringify()).isEqualTo("{\"id\":1,\"title\":\"TITLE\"}");
    }
}
