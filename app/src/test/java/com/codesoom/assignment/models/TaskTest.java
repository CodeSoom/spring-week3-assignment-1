package com.codesoom.assignment.models;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TaskTest {

    private static final long ID = 1L;
    private static final String TITLE = "TASK";

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task(ID, TITLE);
    }

    @Test
    void getId() {
        assertThat(task.getId()).isEqualTo(ID);
    }

    @Test
    void getTitle() {
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Test
    void setTitle() {
        assertThat(task.getTitle()).isEqualTo(TITLE);

        String expectTitle = "NEW";
        task.setTitle(expectTitle);

        assertThat(task.getTitle()).isEqualTo(expectTitle);
    }

    @Test
    @DisplayName("할 일을 JSON 문자로 변환한다")
    void stringify() {
        assertThat(task.stringify()).isEqualTo("{\"id\":" + ID + ",\"title\":\"" + TITLE + "\"}");
    }
}
