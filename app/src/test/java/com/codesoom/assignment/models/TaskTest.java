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
    @DisplayName("객체 사용 테스트")
    void usage() {
        assertThat(task.getId()).isEqualTo(ID);
        assertThat(task.getTitle()).isEqualTo(TITLE);

        String newTitle = "New Title";
        task.setTitle(newTitle);

        assertThat(task.getTitle()).isEqualTo(newTitle);
    }

    @Test
    @DisplayName("할 일을 JSON 문자로 변환한다")
    void stringify() {
        assertThat(task.stringify()).isEqualTo("{\"id\":" + ID + ",\"title\":\"" + TITLE + "\"}");
    }
}
