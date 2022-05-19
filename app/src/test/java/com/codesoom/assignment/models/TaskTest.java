package com.codesoom.assignment.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private Task task;
    private final String TASK_TITLE = "Test Task";
    private final String TASK_TITLE_UPDATED = "Updated Task";
    private final Long TASK_ID = 1L;
    private final Long TASK_ID_UPDATED = 10L;
    @BeforeEach
    void setUp() {
        task = new Task(TASK_ID, TASK_TITLE);
    }

    @Test
    @DisplayName("기본 생성된 Task가 존재하면, getId 요청하면 기본 생성 Task의 id와 같은 값을 반환해야 한다")
    void getId() {
        final Long actual = task.getId();

        assertThat(actual).isEqualTo(TASK_ID);
    }

    @Test
    @DisplayName("기본 생성된 Task에 새로운 Id값을 setId로 요청하면 getId 요청에 따라 새로운 Id값을 반환해야 한다")
    void setId() {
        task.setId(TASK_ID_UPDATED);
        final Long actual = task.getId();

        assertThat(actual).isEqualTo(TASK_ID_UPDATED);
    }

    @Test
    @DisplayName("기본 생성된 Task가 존재하면, gettitle 요청하면 기본 생성 Task의 title와 같은 값을 반환해야 한다")
    void getTitle() {
        final String actual = task.getTitle();

        assertThat(actual).isEqualTo(TASK_TITLE);
    }

    @Test
    @DisplayName("기본 생성된 Task에 새로운 Title을 setTitle로 요청하면 getTitle 요청에 따라 새로운 Title값을 반환해야 한다")
    void setTitle() {
        task.setTitle(TASK_TITLE_UPDATED);
        final String actual = task.getTitle();

        assertThat(actual).isEqualTo(TASK_TITLE_UPDATED);
    }
}
